/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redwerk.likelabs.application.impl.statistics;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.StatisticsService;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.application.dto.statistics.*;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.event.Event;
import com.redwerk.likelabs.domain.model.event.EventRepository;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.ContentTypeFilter;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.service.sn.exception.SNException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl  implements StatisticsService {

    private static final Logger log = Logger.getLogger(StatisticsServiceImpl.class);
    
    @Autowired
    GatewayFactory gatewayFactory;
    
    @Autowired
    private PointService pointService;
    
    @Autowired
    private CompanyService companyService;
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Override
    public Map<StatisticsType, TotalsStatistics> getAllStatistic(long companyId) {
        Company company = companyService.getCompany(companyId);
        
        Map<SocialNetworkType, List<SocialNetworkPost>> posts = new HashMap<SocialNetworkType, List<SocialNetworkPost>>();
        
        IncrementalTotals photosTaken = new IncrementalTotals();
        IncrementalTotals emailsTotals = new IncrementalTotals();

        collectSNStatistic(posts, company);
        collectReviewAndEmailStatistic(posts, photosTaken, emailsTotals, company);

        Map<StatisticsType, TotalsStatistics> resMap = groupByTotals(posts);
        
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter(ParameterType.PHOTOS_TAKEN, photosTaken.getTotals()));
        params.add(new Parameter(ParameterType.FACEBOOK_POSTS, getParameter(resMap.get(StatisticsType.FACEBOOK), ParameterType.FACEBOOK_POSTS)));
        params.add(new Parameter(ParameterType.VKONTAKTE_POSTS, getParameter(resMap.get(StatisticsType.VKONTAKTE), ParameterType.VKONTAKTE_POSTS)));
        params.add(new Parameter(ParameterType.EMAILS, emailsTotals.getTotals()));
        
        resMap.put(StatisticsType.GENERAL, new TotalsStatistics(params));
        return resMap;
    }
    
    private Map<StatisticsType, TotalsStatistics> groupByTotals(Map<SocialNetworkType, List<SocialNetworkPost>> posts){
        Map<StatisticsType, TotalsStatistics> resMap = new HashMap<StatisticsType, TotalsStatistics>();
        Calendar date = new GregorianCalendar();
        for(SocialNetworkType type : posts.keySet()){
            List<SocialNetworkPost> postList = posts.get(type);

            IncrementalTotals postsTotal = new IncrementalTotals();
            IncrementalTotals commentsTotal = new IncrementalTotals();
            IncrementalTotals likesTotal = new IncrementalTotals();
            IncrementalTotals sharesTotal = new IncrementalTotals();
            
            for(SocialNetworkPost post: postList) {
            	date.setTimeInMillis(post.getCreateDate().getTime());
            	postsTotal.increment(1, date);
            	commentsTotal.increment(post.getComments(), date);
            	likesTotal.increment(post.getLikes(), date);
            	sharesTotal.increment(post.getShares(), date);
            }
            List<Parameter> params = new ArrayList<Parameter>();
            params.add(new Parameter(ParameterType.POSTS, postsTotal.getTotals()));
            params.add(new Parameter(ParameterType.COMMENTS, commentsTotal.getTotals()));
            params.add(new Parameter(ParameterType.LIKES, likesTotal.getTotals()));
            params.add(new Parameter(ParameterType.SHARES, sharesTotal.getTotals()));
            resMap.put(convertStatisticType(type), new TotalsStatistics(params));
        }
        return resMap;
    }
    
    private void collectSNStatistic(Map<SocialNetworkType, List<SocialNetworkPost>> posts, Company company){
        User user = company.getAdmins().iterator().next();
        /*if(user==null && user.getAccounts()==null){
            return null;
        }*/
        List<SocialNetworkPost> tmp = null;
        for(CompanySocialPage page : company.getSocialPages()){
                SocialNetworkGateway gateway = gatewayFactory.getGateway(page.getType());
                try {
                    tmp = (List<SocialNetworkPost>)gateway.getStatisticsCompany(page, user.findAccount(page.getType()));
                } catch(SNException ex) {
                    log.error(ex, ex);
                }
                if(tmp !=null){
                    if(!posts.containsKey(page.getType())){
                        posts.put(page.getType(), new ArrayList<SocialNetworkPost>());
                    }
                    posts.get(page.getType()).addAll(tmp);
                }
        }
    }
    
    private void collectReviewAndEmailStatistic(Map<SocialNetworkType, List<SocialNetworkPost>> posts,
                                                IncrementalTotals photosTaken, IncrementalTotals emailsTotals,
                                                Company company){
        
        ReviewQueryData query = new ReviewQueryData(null, null, null, ContentTypeFilter.CONTAINS_PHOTO, Boolean.FALSE, Boolean.FALSE, Pager.ALL_RECORDS, null);
        List<Review> reviews = reviewService.getCompanyReviews(company.getId(), null, query).getItems();
        query = new ReviewQueryData(null, null, null, ContentTypeFilter.CONTAINS_TEXT_AND_PHOTO, Boolean.FALSE, Boolean.FALSE, Pager.ALL_RECORDS, null);
        reviews.addAll(reviewService.getCompanyReviews(company.getId(), null, query).getItems());
        
        List<SocialNetworkPost> tmp = null;
        
        for(Review review : reviews){
            for(UserSocialAccount account : review.getAuthor().getAccounts()){
                    SocialNetworkGateway gateway = gatewayFactory.getGateway(account.getType());
                    try{
                        tmp = (List<SocialNetworkPost>)gateway.getStatisticsUser(account);
                    } catch(SNException ex){
                        log.error(ex, ex);
                    }
                    if(tmp !=null){
                        Calendar tmpDate = new GregorianCalendar();
                        for(SocialNetworkPost post : tmp) {
                            tmpDate.setTimeInMillis(post.getCreateDate().getTime());
                            photosTaken.increment(1, tmpDate);
                        }
                        if(!posts.containsKey(account.getType())){
                            posts.put(account.getType(), new ArrayList<SocialNetworkPost>());
                        }
                        posts.get(account.getType()).addAll(tmp);
                    }
                    
            }
            collectNotifiedEmail(emailsTotals, review.getAuthor());
        }
    }
    
    
    
    private void collectNotifiedEmail(IncrementalTotals emailsTotals, User user){
        List<Event> events = Collections.emptyList();
        Calendar date = new GregorianCalendar();
        for(Event event : events){
            date.setTimeInMillis(event.getNotifiedDT().getTime());
            emailsTotals.increment(1, date);
        }
    }
    
    private StatisticsType convertStatisticType(SocialNetworkType type){
        switch(type) {
            case FACEBOOK: return StatisticsType.FACEBOOK;
            case VKONTAKTE: return StatisticsType.VKONTAKTE;
            default: return StatisticsType.GENERAL;
        }
    }

    public Object getStatisticsCompanyAdmins(long companyId, Interval interval) {
    	
    	
    	Company company = companyService.getCompany(companyId);
    	//company.getAdmins().
    	List<Point> companyPoints = pointService.getPoints(companyId, Pager.ALL_RECORDS).getItems();
    	
        Map<SocialNetworkType, List<SocialNetworkPost>> posts = new HashMap<SocialNetworkType, List<SocialNetworkPost>>();
        Long startDate = new Date().getTime();
        Map<Long, Map<ParameterType, AtomicInteger>> points = new HashMap<Long, Map<ParameterType, AtomicInteger>>();
        for(int i=0; i<30; i++){
        	points.put(startDate-interval.getSec()*i, new HashMap<ParameterType, AtomicInteger>());
        }
        
        for(User user : company.getAdmins()){
            for(UserSocialAccount account : user.getAccounts()){
                if(!posts.containsKey(account.getType())){
                    posts.put(account.getType(), new ArrayList<SocialNetworkPost>());
                }
                posts.get(account.getType()).addAll(getSocialNetworkPosts(account));
            }
        }
        
        Calendar date = new GregorianCalendar();
        Map<SocialNetworkType, TotalsStatistics> statTabl = new HashMap<SocialNetworkType, TotalsStatistics>(); 
        
        Map<Object, ParameterType> parameterMap = new HashMap<Object, ParameterType>();
        parameterMap.put(SocialNetworkType.FACEBOOK, ParameterType.FACEBOOK_POSTS);
        parameterMap.put(SocialNetworkType.VKONTAKTE, ParameterType.VKONTAKTE_POSTS);
        for(SocialNetworkType type : posts.keySet()){
            List<SocialNetworkPost> postList = posts.get(type);
            Collections.sort(postList, new Comparator<SocialNetworkPost>() {
            	@Override
            	public int compare(SocialNetworkPost o1, SocialNetworkPost o2) {
            		return o1.getCreateDate().compareTo(o2.getCreateDate());
            	}
			});
            
            IncrementalTotals postsTotal = new IncrementalTotals();
            IncrementalTotals commentsTotal = new IncrementalTotals();
            IncrementalTotals likesTotal = new IncrementalTotals();
            IncrementalTotals sharesTotal = new IncrementalTotals();
            
            for(SocialNetworkPost post: postList) {
            	date.setTimeInMillis(post.getCreateDate().getTime());
            	postsTotal.increment(1, date);
            	commentsTotal.increment(post.getComments(), date);
            	likesTotal.increment(post.getLikes(), date);
            	sharesTotal.increment(post.getShares(), date);
            	
            	Map<ParameterType, AtomicInteger> curPoints = null;
            	for(Long key : points.keySet()){
            		Long d = post.getCreateDate().getTime();
            		if(d>=(key-interval.getSec())&&d<key){
            			curPoints = points.get(key);  
            			break;
            		}
            	}
            	if(curPoints.containsKey(parameterMap.get(type))){
            		curPoints.get(parameterMap.get(type)).incrementAndGet();
            	} else {
            		curPoints.put(parameterMap.get(type), new AtomicInteger(1));
            	}
            }
            List<Parameter> params = new ArrayList<Parameter>();
            params.add(new Parameter(ParameterType.POSTS, postsTotal.getTotals()));
            params.add(new Parameter(ParameterType.COMMENTS, commentsTotal.getTotals()));
            params.add(new Parameter(ParameterType.LIKES, likesTotal.getTotals()));
            params.add(new Parameter(ParameterType.SHARES, sharesTotal.getTotals()));
            if(!statTabl.containsKey(type)){
            	statTabl.put(type, new TotalsStatistics(params));
            } else {
            	statTabl.put(type, addStatistics(statTabl.remove(type), new TotalsStatistics(params)));
            }
        }
        
        return null;
    }
    
    private List<SocialNetworkPost> getSocialNetworkPosts(UserSocialAccount account){
        return (List<SocialNetworkPost>)gatewayFactory.getGateway(account.getType()).getStatisticsUser(account);
    }
    
    private Totals getParameter(TotalsStatistics statistic, ParameterType type){
        for(Parameter param: statistic.getParameters()){
            if(param.getType()==type){
                return param.getTotals();
            }
        }
        return null;
    }
    
    private TotalsStatistics addStatistics(TotalsStatistics var1, TotalsStatistics var2){
        List<Parameter> result = new ArrayList<Parameter>();
        for(Parameter param1 : var1.getParameters()){
            for(Parameter param2 : var2.getParameters()){
                if(param1.getType()==param2.getType()){
                    result.add(new Parameter(param1.getType(), addTotals(param1.getTotals(), param2.getTotals()) ));
                }
            }
        }
        return new TotalsStatistics(result);
    }

    private Totals addTotals(Totals var1, Totals var2){
        return new Totals(var1.getAll()+var2.getAll(),var1.getLast24Hours()+var2.getLast24Hours(),
                          var1.getLastWeek()+var2.getLastWeek(),var1.getLastMonth()+var2.getLastMonth());
    }

    @Override
    public List<ChartPoint> getChartPionts(long companyId, Interval interval) {
        Company company = companyService.getCompany(companyId);
        ChartBuilder chartBuilder = new ChartBuilder(interval);

        Map<SocialNetworkType, List<SocialNetworkPost>> posts = new HashMap<SocialNetworkType, List<SocialNetworkPost>>();
        collectSNStatistic(posts, company);
        
        for(SocialNetworkType type : posts.keySet()){
            List<SocialNetworkPost> postList = posts.get(type);
            for(SocialNetworkPost post: postList) {
                chartBuilder.addPoint(new ChartPoint(post.getCreateDate(), 0, post.getComments(), post.getLikes(), 0));
            }
        }
        
        collectReviewAndEmailChart(chartBuilder, company);
        return chartBuilder.getPoints();
    }
    
    private void collectReviewAndEmailChart(ChartBuilder chartBuilder, Company company){
        
        ReviewQueryData query = new ReviewQueryData(null, null, null, ContentTypeFilter.CONTAINS_PHOTO, Boolean.FALSE, Boolean.FALSE, Pager.ALL_RECORDS, null);
        List<Review> reviews = reviewService.getCompanyReviews(company.getId(), null, query).getItems();
        query = new ReviewQueryData(null, null, null, ContentTypeFilter.CONTAINS_TEXT_AND_PHOTO, Boolean.FALSE, Boolean.FALSE, Pager.ALL_RECORDS, null);
        reviews.addAll(reviewService.getCompanyReviews(company.getId(), null, query).getItems());
        
        List<SocialNetworkPost> tmp = null;
        
        for(Review review : reviews){
            for(UserSocialAccount account : review.getAuthor().getAccounts()){
                    SocialNetworkGateway gateway = gatewayFactory.getGateway(account.getType());
                    try{
                        tmp = (List<SocialNetworkPost>)gateway.getStatisticsUser(account);
                    } catch(SNException ex){
                        log.error(ex, ex);
                    }
                    if(tmp !=null){
                        for(SocialNetworkPost post : tmp){
                            chartBuilder.addPoint(new ChartPoint(post.getCreateDate(), 1,0,0,0));
                        }
                     
                    }
                    
            }
            
            List<Event> events = Collections.emptyList();
            for(Event event : events){
                chartBuilder.addPoint(new ChartPoint(event.getNotifiedDT(), 0,0,0,1));
            }
        }
        
    }
}
