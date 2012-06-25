package com.redwerk.likelabs.infrastructure.sn;

import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.service.sn.ImageSource;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.service.sn.exception.AccessTokenExpiredException;
import com.redwerk.likelabs.domain.service.sn.exception.ResourceAccessDeniedException;
import com.redwerk.likelabs.domain.service.sn.exception.SNConnectionFailedException;
import com.redwerk.likelabs.domain.service.sn.exception.SNException;
import com.redwerk.likelabs.domain.service.sn.exception.SNResourceType;
import com.redwerk.likelabs.domain.service.sn.exception.WrongAccessCodeException;
import com.redwerk.likelabs.domain.service.sn.exception.WrongPageUrlException;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.service.sn.exception.WrongAccessTokenException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacebookGateway implements SocialNetworkGateway {
    
    private static final Logger log = Logger.getLogger(FacebookGateway.class);
    
    private static final String GET_ACCESS_TOKEN_URL_TEMPLATE = "https://graph.facebook.com/oauth/access_token?client_id={0}&redirect_uri={1}&client_secret={2}&code={3}";

    private static final String clientId = "app.facebook.clientid";

    private static final String clientSecret = "app.facebook.secretkey";
    
    private static final String redirectUri = "/connector/facebook";

    private static final String API_URL = "https://graph.facebook.com/";
    
    private static final Pattern COMPANY_ID_URL_PATTERN = Pattern.compile("(?:facebook\\.com/)([^\\/\\?]+)");
    
    private static final String API_USER_INFO_TEMPLATE = API_URL + "me?access_token={0}";
    
    private static final String API_INFO_TEMPLATE = API_URL + "{0}";
    
    private static final String API_ACCOUNTS_INFO_TEMPLATE = API_URL + "{0}/accounts?access_token={1}";
    
    private static final String API_USER_EMAIL_TEMPLATE = API_URL + "me?fields=email&access_token={0}";
    
    private static final String API_POST_USER_MESSAGE_URL = API_URL + "me/feed";
    
    private static final String API_POST_COMPANY_MESSAGE_TEMPLATE = API_URL + "{0}/feed";
    
    private static final String API_IS_ADMIN_TEMPLATE = API_URL + "{0}?fields=access_token&access_token={1}";

    private static final String MSG_APP_DOMAIN =  "app.domain";

   
    @Autowired
    MessageTemplateService messageTemplateService;

    @Override
    public UserSocialAccount getUserAccountByCode(String code) {
        String url = MessageFormat.format(GET_ACCESS_TOKEN_URL_TEMPLATE, messageTemplateService.getMessage(clientId), 
                                         messageTemplateService.getMessage(MSG_APP_DOMAIN).concat(redirectUri),
                                                          messageTemplateService.getMessage(clientSecret), code);
        String data = requestApiData(url);
        if (!data.contains("access_token")){
            throw new WrongAccessCodeException(code);
        }

        Map<String,String> responseParams = parseResponse(data);
        String accessToken = responseParams.get("access_token");
        return getUserAccountByAccessToken(accessToken);
    }

    @Override
    public UserSocialAccount getUserAccountByAccessToken(String accessToken) {
        String data = requestApiData(MessageFormat.format(API_USER_INFO_TEMPLATE, accessToken));
        
        JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
        
        if (json.containsKey("error")) {
            throw new WrongAccessTokenException(accessToken);
        }
        return new UserSocialAccount(SocialNetworkType.FACEBOOK, json.getString("id"), accessToken, json.getString("name"));
    }

    @Override
    public CompanySocialPage getCompanyPage(String pageUrl) {
        Matcher companyIdMatcher = COMPANY_ID_URL_PATTERN.matcher(pageUrl);
        if (!companyIdMatcher.find()) {
            throw new WrongPageUrlException(pageUrl);
        }
        JSONObject json = requestApiDataJson(MessageFormat.format(API_INFO_TEMPLATE, companyIdMatcher.group(1)));
        if (!json.containsKey("id")) {
            throw new WrongPageUrlException(pageUrl);
        }
        
        if (json.containsKey("link")) {
            String link = json.getString("link");
            if (StringUtils.isNotBlank(link)) {
                pageUrl = link;
            }
        }
        
        return new CompanySocialPage(SocialNetworkType.FACEBOOK, json.getString("id"), pageUrl);
    }

    @Override
    public String getUserEmail(UserSocialAccount account) {
        JSONObject json = requestApiDataJson(MessageFormat.format(API_USER_EMAIL_TEMPLATE, account.getAccessToken()));
        if (!json.containsKey("email")) {
            throw new ResourceAccessDeniedException(SNResourceType.EMAIL_ADDRESS, account);
        }
        return json.getString("email");
    }

    @Override
    public void postUserMessage(UserSocialAccount publisher, String message, ImageSource imageSource) {
        
        String imageUrl = (imageSource != null) ? imageSource.getImageUrl() : "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(API_POST_USER_MESSAGE_URL);
        postMethod.addParameter("message", message);
        postMethod.addParameter("picture", imageUrl);
        postMethod.addParameter("access_token", publisher.getAccessToken());
        try {
            client.executeMethod(postMethod);
            String data = postMethod.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                throw new ResourceAccessDeniedException(SNResourceType.USER_MESSAGE_POSTING, publisher);
            }
        } catch (IOException ex) {
            throw new SNConnectionFailedException(ex);
        }
    }

    @Override
    public void postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message, ImageSource imageSource) {
        String imageUrl = (imageSource != null) ? imageSource.getImageUrl() : "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(MessageFormat.format(API_POST_COMPANY_MESSAGE_TEMPLATE, page.getPageId()));
        postMethod.addParameter("message", message);
        postMethod.addParameter("picture", imageUrl);
        postMethod.addParameter("access_token", publisher.getAccessToken());
        try {
            client.executeMethod(postMethod);
            String data = postMethod.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                throw new ResourceAccessDeniedException(SNResourceType.COMPANY_MESSAGE_POSTING, publisher, page);
            }
        } catch (IOException ex) {
            throw new SNConnectionFailedException(ex);
        }
    }

    @Override
    public void postCompanyMessage(CompanySocialPage page, Company company, String message, ImageSource imageSource) {
        Set<User> users = company.getAdmins();
        
        for (User user : users) {
            UserSocialAccount admin = user.findAccount(SocialNetworkType.FACEBOOK);
            if (admin == null) {
                throw new SNException();
            }
            String url = MessageFormat.format(API_ACCOUNTS_INFO_TEMPLATE, admin.getAccountId(), admin.getAccessToken());
            JSONObject json = requestApiDataJson(url);
            JSONArray socialNetworkPages = json.getJSONArray("data");
            for (Object socialNetworkPage : socialNetworkPages) {
                JSONObject account = (JSONObject) socialNetworkPage;
                String pageId = account.getString("id");
                if (page.getPageId().equals(pageId)) {
                    UserSocialAccount publisher = new UserSocialAccount(SocialNetworkType.FACEBOOK, pageId, account.getString("access_token"), account.getString("name"));
                    postCompanyMessage(page, publisher, message, imageSource);
                    return;
                }
            }
        }
        throw new SNException("Cant publish message to company page");
    }
    
    @Override
    public boolean isAdminFor(UserSocialAccount account, CompanySocialPage page) {
        JSONObject json = requestApiDataJson(MessageFormat.format(API_IS_ADMIN_TEMPLATE, page.getPageId(), account.getAccessToken()));
        if (json.containsKey("error")) {
            throw new SNException();
        }
        return json.containsKey("access_token");
    }
    
    protected String requestApiData(String url) {
        if (StringUtils.isEmpty(url)) return "";
        
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        String data = "";
        try {
            client.executeMethod(getMethod);
            data = getMethod.getResponseBodyAsString();
        } catch (IOException ex) {
            throw new SNConnectionFailedException(ex);
        }
        return data;
    }
    
    public JSONObject requestApiDataJson(String url) {
        String data = requestApiData(url);
        JSONObject json = null;
        try {
            json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                int errorCode = json.getJSONObject("error").getInt("code");
                if (errorCode == 190) {
                    throw new AccessTokenExpiredException(null);
                }
                throw new SNException();
            }
        } catch (JSONException ex) {
            throw new SNException(ex);
        }
        return json;
    }
    
    private static Map<String,String> parseResponse (String response) {
        Map<String,String> params = new HashMap<String, String>();
        String[] responseParts = response.split("&");
        for (String responsePart : responseParts) {
            String[] paramAttrs = responsePart.split("=");
            params.put(paramAttrs[0], paramAttrs[1]);
        }
        return params;
    }
}
