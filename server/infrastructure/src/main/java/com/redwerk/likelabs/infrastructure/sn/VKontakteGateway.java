package com.redwerk.likelabs.infrastructure.sn;

import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.service.sn.ImageSource;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.service.sn.exception.*;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VKontakteGateway implements SocialNetworkGateway {
    
    private static final Logger log = Logger.getLogger(VKontakteGateway.class);
    
    private static final String API_URL = "https://api.vk.com/method/";

    private static final String clientId = "app.vkontakte.clientid";

    private static final String clientSecret = "app.vkontakte.secretkey";

    private static final String GET_ACCESS_TOKEN_URL_TEMPLATE = "https://oauth.vk.com/access_token?client_id={0}&client_secret={1}&code={2}";
    
    private static final Pattern COMPANY_ID_URL_PATTERN = Pattern.compile("(?:vk\\.com/)([^\\/\\?]+)");
    
    private static final String API_USER_INFO_TEMPLATE = API_URL + "users.get?uid={0}";
    
    private static final String API_COMPANY_PAGE_TEMPLATE = API_URL + "groups.getById?gid={0}";
    
    private static final String API_POST_USER_MESSAGE_TEMPLATE = API_URL + "wall.post?owner_id={0}&message={1}&access_token={2}";
    
    private static final String API_POST_COMPANY_MESSAGE_TEMPLATE = API_URL + "wall.post?owner_id={0}&message={1}&access_token={2}";
    
    private static final String API_IS_ADMIN_TEMPLATE = API_URL + "groups.getById?gid={0}&access_token={1}";
    
    private static final String VK_COMPANY_URL_PATTERN = "http://vk.com/{0}";

    @Autowired
    MessageTemplateService messageTemplateService;

    @Override
    public UserSocialAccount getUserAccount(String code) {
        String url = MessageFormat.format(GET_ACCESS_TOKEN_URL_TEMPLATE, messageTemplateService.getMessage(clientId), messageTemplateService.getMessage(clientSecret), code);
        String data = requestApiData(url);

        JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
        if (!json.containsKey("access_token") || !json.containsKey("user_id")) {
            throw new WrongAccessCodeException(code);
        }
        String userId = json.getString("user_id");

        JSONObject accountData = requestApiDataJson(MessageFormat.format(API_USER_INFO_TEMPLATE, userId));

        String name = accountData.getString("first_name") + " " + accountData.getString("last_name");
        return new UserSocialAccount(SocialNetworkType.VKONTAKTE, json.getString("user_id"), json.getString("access_token"), name);
    }

    @Override
    public CompanySocialPage getCompanyPage(String pageUrl) {
        Matcher companyIdMatcher = COMPANY_ID_URL_PATTERN.matcher(pageUrl);
        if (companyIdMatcher.find()) {
            JSONObject json = requestApiDataJson(MessageFormat.format(API_COMPANY_PAGE_TEMPLATE, companyIdMatcher.group(1)));
            if (json.containsKey("gid")) {
                if (json.containsKey("screen_name")) {
                    String screenName = json.getString("screen_name");
                    if (StringUtils.isNotBlank(screenName)) {
                        pageUrl = MessageFormat.format(VK_COMPANY_URL_PATTERN, screenName);
                    }
                }
                return new CompanySocialPage(SocialNetworkType.VKONTAKTE, json.getString("gid"), pageUrl);
            }
        }
        throw new WrongPageUrlException(pageUrl);
    }

    @Override
    public String getUserEmail(UserSocialAccount account) {
        // VK API does not provide data about email
        return null;
    }

    @Override
    public void postUserMessage(UserSocialAccount publisher, String message, ImageSource imageSource) {
        try {
            String url = MessageFormat.format(API_POST_USER_MESSAGE_TEMPLATE, publisher.getAccountId(), URLEncoder.encode(message, "UTF-8"), publisher.getAccessToken());
            String data = requestApiData(url);
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (!json.containsKey("response")) {
                if (json.containsKey("error") && json.getJSONObject("error").containsKey("error_code")) {
                    int errorCode = json.getJSONObject("error").getInt("error_code");
                    if (errorCode == 5) {
                        throw new AccessTokenExpiredException(publisher);
                    }
                    else if (errorCode == 214 || errorCode == 7) {
                        throw new ResourceAccessDeniedException(SNResourceType.USER_MESSAGE_POSTING, publisher);
                    }
                }
                throw new SNGeneralException("Unknown message posting error");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new SNGeneralException(ex);
        }
    }

    @Override
    public void postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message, ImageSource imageSource) {
        try {
            String url = MessageFormat.format(API_POST_COMPANY_MESSAGE_TEMPLATE,  "-" + page.getPageId(), URLEncoder.encode(message, "UTF-8"), publisher.getAccessToken());
            String data = requestApiData(url);
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            
            if (!json.containsKey("response")) {
                if (json.containsKey("error") && json.getJSONObject("error").containsKey("error_code")) {
                    int errorCode = json.getJSONObject("error").getInt("error_code");
                    if (errorCode == 5) {
                        throw new AccessTokenExpiredException(publisher);
                    }
                    else if (errorCode == 214 || errorCode == 7) {
                        throw new ResourceAccessDeniedException(SNResourceType.COMPANY_MESSAGE_POSTING, publisher);
                    }
                }
                throw new SNGeneralException("Unknown company message posting error");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new SNGeneralException(ex);
        }
    }

    @Override
    public boolean isAdminFor(UserSocialAccount account, CompanySocialPage page) {
        JSONObject json = requestApiDataJson(MessageFormat.format(API_IS_ADMIN_TEMPLATE, page.getPageId(), account.getAccessToken()));
        if (json.containsKey("is_admin") && "1".equals(json.getString("is_admin"))) {
            return true;
        }
        return false;
    }
    protected String requestApiData(String url) {
        if (StringUtils.isEmpty(url)) throw new IllegalArgumentException("Url must be not empty");
        
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
    
    public JSONObject requestApiDataJson(String url){
        String data = requestApiData(url);
        JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
        if (json.containsKey("response")) {
            try {
                return json.getJSONArray("response").getJSONObject(0);
            } catch (JSONException ex) {
                throw new SNGeneralException(ex);
            }
        } else if (json.containsKey("error")) {
            if (json.getJSONObject("error").containsKey("error_code")){
                int errorCode = json.getJSONObject("error").getInt("error_code");
                if (errorCode == 5) {
                    throw new AccessTokenExpiredException(null);
                }
            }
            throw new SNGeneralException(json.getJSONObject("error").getString("error_msg"));
        }
        return null;
    }
}
