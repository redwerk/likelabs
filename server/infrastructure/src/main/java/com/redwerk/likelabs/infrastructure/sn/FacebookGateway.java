package com.redwerk.likelabs.infrastructure.sn;

import com.redwerk.likelabs.application.dto.statistics.SocialNetworkPost;
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
import com.redwerk.likelabs.domain.model.post.SNPost;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import com.redwerk.likelabs.domain.service.sn.exception.WrongAccessTokenException;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
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

    private static final String MSG_APP_DOMAIN = "app.domain";

    private static final String API_FEED_TEMPLATE = API_URL + "{0}/feed?fields=id,type,comments,shares,likes,created_time&date_format=U&limit=10000&since={1,number,#}&until={2,number,#}&access_token={3}";

    private static final String API_FEED_USER_TEMPLATE = API_URL + "me/feed?fields=id,type,comments,shares,likes,created_time,application.id&date_format=U&limit=10000&since={0,number,#}&until={1,number,#}&access_token={2}";

    private static final String API_GET_ALBUMS_TEMPLATE = API_URL + "{0}/albums?access_token={1}";

    private static final String API_CREATE_ALBUM_TEMPLATE = API_URL + "{0}/albums";

    private static final String API_UPLOAD_PHOTO_URL = API_URL + "{0}/photos?access_token={1}";

    private static final String PHOTO_ALBUM_NAME = "app.photo.album.name";

    @Autowired
    MessageTemplateService messageTemplateService;

    @Override
    public UserSocialAccount getUserAccountByCode(String code) {
        String url = MessageFormat.format(GET_ACCESS_TOKEN_URL_TEMPLATE, messageTemplateService.getMessage(clientId),
                messageTemplateService.getMessage(MSG_APP_DOMAIN).concat(redirectUri),
                messageTemplateService.getMessage(clientSecret), code);
        String data = requestApiData(url);
        if (!data.contains("access_token")) {
            throw new WrongAccessCodeException(code);
        }

        Map<String, String> responseParams = parseResponse(data);
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
    public String postUserMessage(UserSocialAccount publisher, String message, ImageSource imageSource) {
        if (imageSource != null) {
            uploadPhoto(imageSource.getImageBytes(), publisher.getAccountId(), publisher.getAccessToken(), message);
        }
        String imageUrl = (imageSource != null) ? imageSource.getImageUrl() : "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(API_POST_USER_MESSAGE_URL);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        postMethod.addParameter("message", message);
        postMethod.addParameter("picture", imageUrl);
        postMethod.addParameter("access_token", publisher.getAccessToken());
        try {
            client.executeMethod(postMethod);
            String data = postMethod.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                log.error(json.getString("error"));
                throw new ResourceAccessDeniedException(SNResourceType.USER_MESSAGE_POSTING, publisher);
            }
            return json.getString("id");
        } catch (IOException e) {
            throw new SNConnectionFailedException(e);
        }
    }

    @Override
    public String postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message, ImageSource imageSource) {
        if (imageSource != null) {
            String accessToken = page.getPageId().equalsIgnoreCase(publisher.getAccountId()) ? publisher.getAccessToken() : getPageAdminAccessToken(page, publisher);
            uploadPhoto(imageSource.getImageBytes(), page.getPageId(), accessToken, message);
        }
        String imageUrl = (imageSource != null) ? imageSource.getImageUrl() : "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(MessageFormat.format(API_POST_COMPANY_MESSAGE_TEMPLATE, page.getPageId()));
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        message = message != null ? message : "";
        postMethod.addParameter("message", message);
        postMethod.addParameter("picture", imageUrl);
        postMethod.addParameter("access_token", publisher.getAccessToken());
        try {
            client.executeMethod(postMethod);
            String data = postMethod.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                log.error(json.getString("error"));
                throw new ResourceAccessDeniedException(SNResourceType.COMPANY_MESSAGE_POSTING, publisher, page);
            }
            return json.getString("id");
        } catch (IOException ex) {
            log.error(ex, ex);
            throw new SNConnectionFailedException(ex);
        }
    }

    @Override
    public String postCompanyMessage(CompanySocialPage page, Company company, String message, ImageSource imageSource) {
        String accessToken = getPageAdminAccessToken(page, company);
        if (accessToken == null) {
            throw new SNException("Cant publish message to company page");
        }

        UserSocialAccount publisher = new UserSocialAccount(SocialNetworkType.FACEBOOK, page.getPageId(), accessToken, page.getUrl());
        return postCompanyMessage(page, publisher, message, imageSource);

    }

    private String getPageAdminAccessToken(CompanySocialPage page, Company company) {
        Set<User> users = company.getAdmins();

        for (User user : users) {
            UserSocialAccount admin = user.findAccount(SocialNetworkType.FACEBOOK);
            if (admin == null) {
                throw new SNException();
            }
            return getPageAdminAccessToken(page, admin);
        }
        return null;
    }

    private String getPageAdminAccessToken(CompanySocialPage page, UserSocialAccount admin) {
        String url = MessageFormat.format(API_ACCOUNTS_INFO_TEMPLATE, admin.getAccountId(), admin.getAccessToken());
        JSONObject json = requestApiDataJson(url);
        JSONArray socialNetworkPages = json.getJSONArray("data");
        for (Object socialNetworkPage : socialNetworkPages) {
            JSONObject account = (JSONObject) socialNetworkPage;
            String pageId = account.getString("id");
            if (page.getPageId().equals(pageId)) {
                return account.getString("access_token");
            }
        }
        return null;
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
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Url must not be empty");
        }

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

    private static Map<String, String> parseResponse(String response) {
        Map<String, String> params = new HashMap<String, String>();
        String[] responseParts = response.split("&");
        for (String responsePart : responseParts) {
            String[] paramAttrs = responsePart.split("=");
            params.put(paramAttrs[0], paramAttrs[1]);
        }
        return params;
    }

    @Override
    public List<SocialNetworkPost> getStatisticsCompany(CompanySocialPage page, UserSocialAccount account) {
        Date currentDate = new Date();
        long since = 0l;
        long until = currentDate.getTime() / 1000;
        String feedUrl = MessageFormat.format(API_FEED_TEMPLATE, page.getPageId(), since, until, account.getAccessToken());
        JSONObject json = requestApiDataJson(feedUrl);
        JSONArray data = json.getJSONArray("data");
        if (data.size() < 1) {
            return null;
        }
        long ourAppId = Long.parseLong(messageTemplateService.getMessage(clientId));

        List<SocialNetworkPost> result = new ArrayList<SocialNetworkPost>();
        for (Object item : data) {
            JSONObject feed = (JSONObject) item;
            if (feed.getJSONObject("application").getLong("id") == ourAppId) {
                result.add(new SocialNetworkPost(new Date(),
                        feed.getJSONObject("shares").getInt("count"),
                        feed.getJSONObject("comments").getInt("count"),
                        feed.getJSONObject("likes").getInt("count")));
            }
        }
        return result;
    }

    @Override
    public List<SocialNetworkPost> getStatisticsUser(UserSocialAccount account) {
        Date currentDate = new Date();
        long since = 0l;
        long until = currentDate.getTime() / 1000;
        String feedUrl = MessageFormat.format(API_FEED_USER_TEMPLATE, since, until, account.getAccessToken());
        JSONObject json = requestApiDataJson(feedUrl);
        JSONArray data = json.getJSONArray("data");
        if (data.size() < 1) {
            return null;
        }
        long ourAppId = Long.parseLong(messageTemplateService.getMessage(clientId));

        List<SocialNetworkPost> result = new ArrayList<SocialNetworkPost>();
        for (Object item : data) {
            JSONObject feed = (JSONObject) item;
            if (feed.getJSONObject("application").getLong("id") == ourAppId) {
                result.add(new SocialNetworkPost(new Date(),
                        feed.getJSONObject("shares").getInt("count"),
                        feed.getJSONObject("comments").getInt("count"),
                        feed.getJSONObject("likes").getInt("count")));
            }
        }
        return result;
    }

    @Override
    public Object getUserStatistics(UserSocialAccount account, List<SNPost> posts) {
        Date currentDate = new Date();
        long since = 0l;
        long until = currentDate.getTime() / 1000;
        String feedUrl = MessageFormat.format(API_FEED_USER_TEMPLATE, since, until, account.getAccessToken());
        JSONObject json = requestApiDataJson(feedUrl);
        JSONArray data = json.getJSONArray("data");
        if (data.size() < 1) {
            return null;
        }

        List<SocialNetworkPost> result = new ArrayList<SocialNetworkPost>();
        for (Object item : data) {
            JSONObject feed = (JSONObject) item;
            if (findSNId(feed.getString("id"), posts)) {
                result.add(new SocialNetworkPost(new Date(),
                        getCount(feed, "shares"),
                        getCount(feed, "comments"),
                        getCount(feed, "likes")));
            }
            if (posts.isEmpty()) {
                break;
            }
        }
        return result;
    }

    private int getCount(JSONObject json, String key) {
        if (json.containsKey(key)) {
            JSONObject likes = json.getJSONObject(key);
            int val = likes.getInt("count");
            return val;
        }
        return 0;
    }

    private boolean findSNId(String snId, List<SNPost> posts) {
        for (SNPost post : posts) {
            if (post.getSnPostId().equals(snId)) {
                posts.remove(post);
                return true;
            }
        }
        return false;
    }

    public String uploadPhoto(byte[] image, String profileId, String accessToken, String message) {
        String albumId = getAlbumId(messageTemplateService.getMessage(PHOTO_ALBUM_NAME), profileId, accessToken);
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(MessageFormat.format(API_UPLOAD_PHOTO_URL, albumId, accessToken));
        if (message != null) {
            postMethod.addParameter("message", message);
        }
        PartSource imagePartSource = new ByteArrayPartSource("test.png", image);
        Part[] parts = {
            new FilePart("source", imagePartSource)
        };
        postMethod.setRequestEntity(
                new MultipartRequestEntity(parts, postMethod.getParams()));
        try {
            client.executeMethod(postMethod);
            String data = postMethod.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                log.error(json.getString("error"));
                throw new SNException(json.getString("error"));
            }
            return json.getString("id");
        } catch (IOException ex) {
            log.error(ex, ex);
            throw new SNConnectionFailedException(ex);
        }
    }

    public String getAlbumId(String title, String profileId, String accessToken) {
        String url = MessageFormat.format(API_GET_ALBUMS_TEMPLATE, profileId, accessToken);
        JSONObject json = requestApiDataJson(url);
        JSONArray albums = json.getJSONArray("data");
        if (albums.size() > 0) {
            for (Object item : albums) {
                JSONObject album = (JSONObject) item;
                if (title.equalsIgnoreCase(album.getString("name"))) {
                    return album.getString("id");
                }
            }
        }
        return createAlbum(title, profileId, accessToken);
    }

    private String createAlbum(String title, String profileId, String accessToken) {
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(MessageFormat.format(API_CREATE_ALBUM_TEMPLATE, profileId));
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        postMethod.addParameter("name", title);
        postMethod.addParameter("access_token", accessToken);
        try {
            client.executeMethod(postMethod);
            String data = postMethod.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("error")) {
                log.error(json.getString("error"));
                throw new SNException(json.getString("error"));
            }
            return json.getString("id");
        } catch (IOException ex) {
            log.error(ex, ex);
            throw new SNConnectionFailedException(ex);
        }
    }
}
