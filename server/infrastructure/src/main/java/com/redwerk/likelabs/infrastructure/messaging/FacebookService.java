package com.redwerk.likelabs.infrastructure.messaging;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class FacebookService extends SocialNetworkService {
    
    private static final Logger log = Logger.getLogger(FacebookService.class);
    
    private static final String FB_API_URL = "https://graph.facebook.com";
    
    private JSONObject profileData = null;
    
    public FacebookService(String profileId, String access_token) {
        super(profileId, access_token, FB_API_URL);
    }

    @Override
    public String getUserName() throws Exception {
        return getProfileData().getString("name");
    }

    @Override
    public String getEmail() throws Exception {
        return getFields("email").getString("email");
    }
    
    @Override
    public String publishMessage(String message) throws Exception {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("message", message));
        
        NameValuePair[] parameters = list.toArray(new NameValuePair[0]);
        
        String data = postData(profileId + "/feed", parameters);
        return data;
    }

    @Override
    public boolean checkIsAdmin(String companyId) throws Exception {
        String data = getData("me/accounts");
        JSONObject accountData = (JSONObject) (new JSONTokener(data)).nextValue();
        JSONArray accounts = accountData.getJSONArray("data");
        if (accounts.isEmpty()) return false;
        
        if (!StringUtils.isNumeric(companyId)) {
            String companyData = getData(companyId);
            JSONObject companyJsonData = (JSONObject) (new JSONTokener(companyData)).nextValue();
            companyId = companyJsonData.getString("id");
        }
        
        for (Object companyObject : accounts) {
            JSONObject company = (JSONObject) companyObject;
            if (companyId.equalsIgnoreCase(company.getString("id"))) return true;
        }
        return false;
    }

    public JSONObject getProfileData() throws Exception {
        if (profileData == null) {
            String data = getData(profileId);
            profileData = (JSONObject) (new JSONTokener(data)).nextValue();
        }
        return profileData;
    }
    
    public JSONObject getFields(String fields) throws Exception {
        String data = getData(profileId + "?fields=" + fields);
        JSONObject fieldsData = (JSONObject) (new JSONTokener(data)).nextValue();
        return fieldsData;
    }
}
