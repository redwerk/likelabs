package com.redwerk.likelabs.infrastructure.messaging;



import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.jsmpp.util.RelativeTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redwerk.likelabs.application.messaging.SmsService;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException;
import com.redwerk.likelabs.application.messaging.exception.SmsMessagingException.DeliveryStatus;


public class SmppSmsServiceImpl implements SmsService {

	private static final Logger LOG = LoggerFactory.getLogger(SmppSmsServiceImpl.class);
	
    private static final long SESSION_INACTIVE_TIMEOUT_MS = 15 * 60000;	//SMPP session is closed if no activity detected for this time
	private static final long SESSION_CHECK_INTERVAL_MS = 2000;	//period of checking for SMPP session state
	private static final long SMSC_TIMEOUT_MS = 2000;	//timeout for low-level SMMP transactions
	private static final int SM_VALIDITY_PERIOD_MIN = 10;	//after expiration of this period SMSC should discard not delivered messages 

    @Value("#{applicationProperties['sms.service.smpp.username']}")
    private String username;

    @Value("#{applicationProperties['sms.service.smpp.password']}")
    private String password;    
    
    @Value("#{applicationProperties['sms.service.smpp.host']}")
    private String remoteHost;
    
    @Value("#{applicationProperties['sms.service.smpp.port']}")
    private int remotePort;
     
    @Value("#{applicationProperties['sms.service.smpp.source.address']}")
    private String sourceAddress;
    
    @Value("#{applicationProperties['sms.service.smpp.system.type']}")
    private String systemType;	//optional by specification (some SMSC may require)
   
    
    private String addressRangeBindTo;	//regex of addresses to listen for incoming messages    
    private final Object smppSessionLock = new Object();
    private SMPPSession smppSession;
    

    public SmppSmsServiceImpl() {
    }    
    
    @Override
    public void sendMessage(String phone, String msgText) {
    	synchronized(smppSessionLock) {
    		startSmppSessionIfNotExists();
	        try {	        	
	        	LOG.debug("Submitting message: phone= '{}'", phone);
	            String messageId = smppSession.submitShortMessage("CMT", 
	            		TypeOfNumber.ALPHANUMERIC, NumberingPlanIndicator.UNKNOWN, 
	            		sourceAddress, 
	            		TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, 
	            		phone, 
	            		new ESMClass(), 
	            		(byte)0, (byte)1,  
	            		null,	//sheduled delivery time 
	            		RelativeTimeFormatter.format(0, 0, 0, 0, SM_VALIDITY_PERIOD_MIN, 0),	//validity period
	            		new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE), 
	            		(byte)0, 
	            		new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), 
	            		(byte)0, 
	            		msgText.getBytes());
				LOG.info("Message submitted: messageId={}, phone= '{}'", messageId, phone);
			} catch (PDUException e) {
			    throw new SmsMessagingException(DeliveryStatus.GENERAL_ERROR, phone, "Submiting SM failed: invalid PDU parameter", e);
			} catch (ResponseTimeoutException e) {
			    throw new SmsMessagingException(DeliveryStatus.TIMEOUT, phone, "Submiting SM failed: response timeout", e);
			} catch (InvalidResponseException e) {
			    throw new SmsMessagingException(DeliveryStatus.REMOTE_ERROR, phone, "Submiting SM failed: received invalid respose", e);
			} catch (NegativeResponseException e) {
			    throw new SmsMessagingException(DeliveryStatus.REMOTE_ERROR, phone, "Submiting SM failed: received negative response", e);
			} catch (IOException e) {
			    throw new SmsMessagingException(DeliveryStatus.NETWORK_ERROR, phone, "Submiting SM failed", e);
			}   		
    	}
    }    

    
    
    
    private void startSmppSessionIfNotExists() {
 		if(smppSession == null) {
	        try {
	        	smppSession = new SMPPSession();
	 	        DeliveryReceiptListener deliveryListener = new DeliveryReceiptListener();
	        	BindParameter bindParam = new BindParameter(BindType.BIND_TRX, username, password, systemType, 
	        			TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN,	addressRangeBindTo != null ? addressRangeBindTo : sourceAddress);        
	 	        smppSession.setMessageReceiverListener(deliveryListener);
	 	        smppSession.setTransactionTimer(SMSC_TIMEOUT_MS);
	        	LOG.debug("Starting SMPP session");
	        	smppSession.connectAndBind(remoteHost, remotePort, bindParam, SMSC_TIMEOUT_MS);
	        	LOG.debug("SMPP session connected and binded: sessionId={}", smppSession.getSessionId());
	        	new SessionActivityMonitor().start();
	        } catch (IOException e) {
	        	smppSession = null;
	            throw new SmsMessagingException(DeliveryStatus.GENERAL_ERROR, null, "Starting SMPP session failed", e);
	        }
    	}   	
    }
    
    
    /**
     * Serves to close SMPP session if not used more than {@link SESSION_INACTIVE_TIMEOUT_MS}.
     * 
     * <p>After started with {@link #start()} check session state periodically with interval {@link SESSION_CHECK_INTERVAL_MS}.
     */
    private class SessionActivityMonitor extends TimerTask {
    	private final Timer timer;
    	
    	public SessionActivityMonitor() {
    		timer = new Timer();
    	}
    	
    	public void start() {
    		timer.schedule(this, 0, SESSION_CHECK_INTERVAL_MS);   		
    	}
    	
		@Override
		public void run() {
			synchronized(smppSessionLock) {
				if(smppSession != null && System.currentTimeMillis() - smppSession.getLastActivityTimestamp() > SESSION_INACTIVE_TIMEOUT_MS) {
					LOG.info("SMPP session was inactive more than {} ms, closing: sessionId={}", SESSION_INACTIVE_TIMEOUT_MS, smppSession.getSessionId());
					try {
						smppSession.unbindAndClose();
					} catch(Exception ex) {
						LOG.error("Error closing SMPP session, sessionId={}", smppSession.getSessionId(), ex);
					} finally {
						smppSession = null;
						timer.cancel();
					}
				}
			}
		}   
    }
    
    
    
    
	/**
	 * Captures SM delivery receipts sent by SMSC.
	 * 
	 * <p>Intended to be attached to {@link SMPPSession}. 
	 * Execution of overridden methods should not take long, since it can block low-level SMPP communications. 
	 */
	private static class DeliveryReceiptListener implements MessageReceiverListener {
		
		private static final Logger LOG = LoggerFactory.getLogger(DeliveryReceiptListener.class);
		
		@Override
		public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
			if (deliverSm.isSmscDeliveryReceipt()) {
				try {
					DeliveryReceipt deliveryReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
					if(LOG.isInfoEnabled()) {
						LOG.info("Received delivery receipt: messageId={}, status : {}, src: '{}', dest: '{}'", 
								new Object[]{deliveryReceipt.getId(), deliveryReceipt.getFinalStatus(), deliverSm.getSourceAddr(), deliverSm.getDestAddress()});
					}
				} catch (InvalidDeliveryReceiptException e) {
					LOG.error("Invalid delivery receipt", e);
				}
			} else {
				//regular short message
				if(LOG.isInfoEnabled()) {
					LOG.info("Got message from '{}' : {}", deliverSm.getSourceAddr(), new String(deliverSm.getShortMessage()));
				}
			}
		}

		@Override
		public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
			LOG.info("Got data message from '{}' : {}", dataSm.getSourceAddr(), dataSm.getCommandId());
			return null;
		}

		@Override
		public void onAcceptAlertNotification(AlertNotification alertNotification) {
			LOG.info("Got alert notification from '{}' : {}", alertNotification.getSourceAddr(), alertNotification.getCommandId());
		}
	}
 }
