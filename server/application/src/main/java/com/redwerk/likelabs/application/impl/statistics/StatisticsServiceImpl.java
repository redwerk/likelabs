package com.redwerk.likelabs.application.impl.statistics;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.StatisticsService;
import com.redwerk.likelabs.application.dto.statistics.*;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.post.*;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewRepository;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.service.sn.GatewayFactory;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.service.sn.exception.SNException;
import java.util.*;
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
    private ReviewRepository reviewRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Override
    public TotalsStatistics getStatistics(long companyId, StatisticsType statisticsType) {
        Company company = companyService.getCompany(companyId);
        
        switch(statisticsType){
            case FACEBOOK:
                return getDetailed(company, SocialNetworkType.FACEBOOK);
            case VKONTAKTE:
                return getDetailed(company, SocialNetworkType.VKONTAKTE);
            default:
                return getGeneral(company);
        }
    }
    
    private TotalsStatistics getDetailed(Company company, SocialNetworkType type) {
        
        Map<UserSocialAccount, List<SNPost>> posts = new HashMap<UserSocialAccount, List<SNPost>>();
        
        IncrementalTotals postsTotals = new IncrementalTotals();
        IncrementalTotals commentsTotals = new IncrementalTotals();
        IncrementalTotals likesTotals = new IncrementalTotals();
        IncrementalTotals sharesTotals = new IncrementalTotals();
        
        Calendar date = new GregorianCalendar();
        
        for (Post post : postRepository.findAll(company, PostTypeFilter.SN_WALL)) {
            date.setTime(post.getCreated());
            SNPost snPost = (SNPost) post; 
            
            if (snPost.getSnType()==type) {
                postsTotals.increment(1, date);
                UserSocialAccount account = snPost.getRecipient().findAccount(type);
                if (!posts.containsKey(account)) {
                    posts.put(account, new ArrayList<SNPost>());
                } 
                posts.get(account).add(snPost);
            } 
        }
        
        SocialNetworkGateway gateway = gatewayFactory.getGateway(type);
        for(UserSocialAccount account : posts.keySet()){
            List<SocialNetworkPost> snpPosts = new ArrayList<SocialNetworkPost>();
            try {
                snpPosts = (List<SocialNetworkPost>) gateway.getUserStatistics(account, posts.get(account));
            } catch (SNException e) {
                log.error(e, e);
            }
            for(SocialNetworkPost post : snpPosts) {
                date.setTime(post.getCreateDate());
                commentsTotals.increment(post.getComments(), date);
                likesTotals.increment(post.getLikes(), date);
                sharesTotals.increment(post.getShares(), date);
            }
        }
        
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter(ParameterType.POSTS, postsTotals.getTotals()));
        params.add(new Parameter(ParameterType.COMMENTS, commentsTotals.getTotals()));
        params.add(new Parameter(ParameterType.LIKES, likesTotals.getTotals()));
        params.add(new Parameter(ParameterType.SHARES, sharesTotals.getTotals()));
        
        return new TotalsStatistics(params);  
    }
    
    private TotalsStatistics getGeneral(Company company){
    
        Map<SocialNetworkType, List<SNPost>> posts = new HashMap<SocialNetworkType, List<SNPost>>();
        
        IncrementalTotals photosTaken = new IncrementalTotals();
        IncrementalTotals emailsTotals = new IncrementalTotals();
        IncrementalTotals facebookTotal = new IncrementalTotals();
        IncrementalTotals vkontakteTotal = new IncrementalTotals();
        
        Calendar date = new GregorianCalendar();
        
        for(Post post : postRepository.findAll(company, PostTypeFilter.ALL)) {
            date.setTime(post.getCreated());
            if(post instanceof SNPost) {
                SNPost snPost = (SNPost) post; 
                switch(snPost.getSnType()){
                    case FACEBOOK: 
                        facebookTotal.increment(1, date);
                        break;
                    case VKONTAKTE:
                        vkontakteTotal.increment(1, date);
                }
                if(!posts.containsKey(snPost.getSnType())){
                    posts.put(snPost.getSnType(), new ArrayList<SNPost>());
                }
                posts.get(snPost.getSnType()).add(snPost);
            } else {
                emailsTotals.increment(1, date);
            }
        }
       
        for(Review review : reviewRepository.findPhotoReviews(company)){
            date.setTime(review.getCreatedDT());
            photosTaken.increment(1, date);
        }
        
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new Parameter(ParameterType.PHOTOS_TAKEN, photosTaken.getTotals()));
        params.add(new Parameter(ParameterType.FACEBOOK_POSTS, facebookTotal.getTotals()));
        params.add(new Parameter(ParameterType.VKONTAKTE_POSTS, vkontakteTotal.getTotals()));
        params.add(new Parameter(ParameterType.EMAILS, emailsTotals.getTotals()));
        
        return new TotalsStatistics(params);    
    
    }
   
    @Override
    public List<ChartPoint> getChartPoints(long companyId, Interval interval) {
        Company company = companyService.getCompany(companyId);
        
        List<Post> reviewPosts = postRepository.findAll(company, PostTypeFilter.ALL);
        
        ChartBuilder chartBuilder = new ChartBuilder(interval);

        Calendar date = new GregorianCalendar();
        
        for(Post post : reviewPosts) {
            date.setTime(post.getCreated());
            SNPost snPost = null;
            int emails = 0;
            int facebook = 0;
            int vkontakte =0;
            try {
                snPost = (SNPost) post; 
            } catch(ClassCastException e) {
                //
            }
            
            if(snPost != null){
                switch(snPost.getSnType()){
                    case FACEBOOK: 
                        facebook = 1;
                        break;
                    case VKONTAKTE:
                        vkontakte = 1;
                }
            } else {
                EmailPost emailPost = null;
                try {
                    emailPost = (EmailPost) post;
                } catch(ClassCastException e) {
                    //
                }
                if(emailPost != null){
                    emails = 1;
                }
            }
            chartBuilder.addPoint(new ChartPoint(post.getCreated(), 0, facebook, vkontakte, emails));
        }
        List<Review> reviews = reviewRepository.findPhotoReviews(company);
        
        for(Review review : reviews){
            date.setTime(review.getCreatedDT());
            chartBuilder.addPoint(new ChartPoint(review.getCreatedDT(), 1, 0, 0, 0));
        }
        
        return chartBuilder.getPoints();
    }


}