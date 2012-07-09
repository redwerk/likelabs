package com.redwerk.likelabs.infrastructure.sn;

import com.redwerk.likelabs.application.dto.statistics.IncrementalTotals;
import com.redwerk.likelabs.application.dto.statistics.Parameter;
import com.redwerk.likelabs.application.dto.statistics.ParameterType;
import com.redwerk.likelabs.application.dto.statistics.SocialNetworkPost;
import com.redwerk.likelabs.application.dto.statistics.TotalsStatistics;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.service.sn.ImageSource;
import com.redwerk.likelabs.domain.service.sn.SocialNetworkGateway;
import com.redwerk.likelabs.domain.service.sn.exception.*;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.post.SNPost;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserSocialAccount;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class VKontakteGateway implements SocialNetworkGateway {

    private static final Logger log = Logger.getLogger(VKontakteGateway.class);

    private static final String API_URL = "https://api.vk.com/method/";

    private static final String clientId = "app.vkontakte.clientid";

    private static final String clientSecret = "app.vkontakte.secretkey";

    private static final String GET_ACCESS_TOKEN_URL_TEMPLATE = "https://oauth.vk.com/access_token?client_id={0}&client_secret={1}&code={2}";

    private static final Pattern COMPANY_ID_URL_PATTERN = Pattern.compile("(?:vk\\.com/)([^\\/\\?]+)");

    private static final String API_USER_INFO_TEMPLATE = API_URL + "users.get?uid={0}";

    private static final String API_COMPANY_PAGE_TEMPLATE = API_URL + "groups.getById?gid={0}";

    private static final String API_POST_USER_MESSAGE_TEMPLATE = API_URL + "wall.post?owner_id={0}&message={1}&attachments={2}&access_token={3}";

    private static final String API_POST_COMPANY_MESSAGE_TEMPLATE = API_URL + "wall.post?owner_id={0}&message={1}&attachments={2}&access_token={3}";

    private static final String API_IS_ADMIN_TEMPLATE = API_URL + "groups.getById?gid={0}&access_token={1}";

    private static final String API_GET_UPLOAD_SERVER = API_URL + "photos.getUploadServer?aid={0}&gid={1}&access_token={2}";

    private static final String API_SAVE_PHOTO = API_URL + "photos.save?aid={0}&gid={1}&server={2}&photos_list={3}&hash={4}&access_token={5}";

    private static final String API_GET_ALBUMS_FOR_USER = API_URL + "photos.getAlbums?uid={0}&access_token={1}";

    private static final String API_GET_ALBUMS_FOR_GROUP = API_URL + "photos.getAlbums?gid={0}&access_token={1}";

    private static final String API_CREATE_ALBUM = API_URL + "photos.createAlbum?gid={0}&title={1}&access_token={2}";

    private static final String API_GET_USER_ID_TEMPLATE = API_URL + "getUserInfo?access_token={0}";

    private static final String VK_COMPANY_URL_PATTERN = "http://vk.com/{0}";

    private static final String API_STATISTICS_URL_TEMPLATE = API_URL + "wall.get?owner_id={0}&offset={1,number,#}&count=100&access_token={2}";

    private static final String PHOTO_ALBUM_NAME = "app.photo.album.name";

    @Autowired
    MessageTemplateService messageTemplateService;

    @Override
    public UserSocialAccount getUserAccountByCode(String code) {
        String url = MessageFormat.format(GET_ACCESS_TOKEN_URL_TEMPLATE, messageTemplateService.getMessage(clientId), messageTemplateService.getMessage(clientSecret), code);
        String data = requestApiData(url);

        JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
        if (!json.containsKey("access_token") || !json.containsKey("user_id")) {
            throw new WrongAccessCodeException(code);
        }
        return getUserSocialAccount(json.getString("access_token"), json.getString("user_id"));
    }

    @Override
    public UserSocialAccount getUserAccountByAccessToken(String accessToken) {
        String url = MessageFormat.format(API_GET_USER_ID_TEMPLATE, accessToken);
        JSONObject json = requestApiDataJson(url);
        return getUserSocialAccount(accessToken, json.getString("user_id"));
    }

    private UserSocialAccount getUserSocialAccount(String accessToken, String userId) {
        JSONObject accountData = requestApiDataJson(MessageFormat.format(API_USER_INFO_TEMPLATE, userId));
        String name = accountData.getString("first_name") + " " + accountData.getString("last_name");
        return new UserSocialAccount(SocialNetworkType.VKONTAKTE, userId, accessToken, name);
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
    public String postUserMessage(UserSocialAccount publisher, String message, ImageSource imageSource) {
        try {
            String photoId = "";
            if (imageSource != null) {
                String albumId = getAlbumId(publisher, messageTemplateService.getMessage(PHOTO_ALBUM_NAME));
                photoId = uploadPhoto(imageSource.getImageBytes(), publisher, albumId);
            }

            String url = MessageFormat.format(API_POST_USER_MESSAGE_TEMPLATE, publisher.getAccountId(), URLEncoder.encode(message, "UTF-8"), photoId, publisher.getAccessToken());
            String data = requestApiData(url);
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (!json.containsKey("response")) {
                if (json.containsKey("error") && json.getJSONObject("error").containsKey("error_code")) {
                    int errorCode = json.getJSONObject("error").getInt("error_code");
                    if (errorCode == 5) {
                        log.error(json.getString("error"));
                        throw new AccessTokenExpiredException(publisher);
                    } else if (errorCode == 214 || errorCode == 7) {
                        log.error(json.getString("error"));
                        throw new ResourceAccessDeniedException(SNResourceType.USER_MESSAGE_POSTING, publisher);
                    }
                }
                log.error(json.getString("error"));
                throw new SNException("Unknown message posting error");
            } else {
                JSONObject res = json.getJSONObject("response");
                String postId = res.getString("post_id");
                return postId;
            }
        } catch (UnsupportedEncodingException ex) {
            throw new SNException(ex);
        }
    }

    /**
     * Upload image to user's default album
     * @param image
     * @param publisher 
     * @return Photo ID
     */
    public String uploadPhoto(byte[] image, UserSocialAccount publisher, String albumId) {
        return uploadPhoto(image, publisher, albumId, null);
    }

    public String uploadPhoto(byte[] image, UserSocialAccount publisher, String albumId, CompanySocialPage page) {
        JSONObject server = requestApiDataJson(MessageFormat.format(API_GET_UPLOAD_SERVER, albumId, page != null ? page.getPageId() : "", publisher.getAccessToken()));
        String uploadUrl = server.getString("upload_url");
        try {
            PostMethod post = new PostMethod(uploadUrl);
            PartSource imagePartSource = new ByteArrayPartSource("test.png", image);
            Part[] parts = {
                // do not change "file1" - this is required field name for VK API
                new FilePart("file1", imagePartSource)
            };
            post.setRequestEntity(
                    new MultipartRequestEntity(parts, post.getParams()));
            HttpClient client = new HttpClient();
            client.executeMethod(post);

            String body = post.getResponseBodyAsString();
            JSONObject json = (JSONObject) (new JSONTokener(body)).nextValue();
            String serverId = json.getString("server");
            String photosList = json.getString("photos_list");
            String hash = json.getString("hash");

            String savePhotoUrl = MessageFormat.format(API_SAVE_PHOTO, albumId, page != null ? page.getPageId() : "", serverId, URLEncoder.encode(photosList, "UTF-8"), hash, publisher.getAccessToken());
            JSONObject photoResponse = requestApiDataJson(savePhotoUrl);
            return photoResponse.getString("id");
        } catch (IOException ex) {
            log.debug(ex, ex);
        }
        return "";
    }

    public String getAlbumId(UserSocialAccount account, String title) {
        return getAlbumId(account, title, null);
    }

    public String getAlbumId(UserSocialAccount account, String title, CompanySocialPage page) {
        String url;
        if (page != null) {
            url = MessageFormat.format(API_GET_ALBUMS_FOR_GROUP, page.getPageId(), account.getAccessToken());
        } else {
            url = MessageFormat.format(API_GET_ALBUMS_FOR_USER, account.getAccountId(), account.getAccessToken());
        }
        String data = requestApiData(url);
        JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
        if (json.containsKey("response")) {
            try {
                JSONArray albums = json.getJSONArray("response");
                if (albums.size() < 1) {
                    return createAlbum(account, title, page);
                }
                for (Object item : albums) {
                    JSONObject album = (JSONObject) item;
                    if (title.equalsIgnoreCase(album.getString("title"))) {
                        return album.getString("aid");
                    }
                }
            } catch (JSONException ex) {
                throw new SNException(ex);
            }
        }
        return createAlbum(account, title, page);
    }

    public String createAlbum(UserSocialAccount account, String title, CompanySocialPage page) {
        String url = MessageFormat.format(API_CREATE_ALBUM, page != null ? page.getPageId() : "", URLEncoder.encode(title), account.getAccessToken());
        JSONObject album = requestApiDataJson(url);
        return album.getString("aid");
    }

    @Override
    public String postCompanyMessage(CompanySocialPage page, Company company, String message, ImageSource imageSource) {
        Set<User> users = company.getAdmins();

        for (User user : users) {
            UserSocialAccount admin = user.findAccount(SocialNetworkType.VKONTAKTE);
            if (admin == null) {
                throw new SNException();
            }
            return postCompanyMessage(page, admin, message, imageSource);
        }
        throw new SNException("Can't publish message to company page");
    }

    @Override
    public String postCompanyMessage(CompanySocialPage page, UserSocialAccount publisher, String message, ImageSource imageSource) {
        try {
            String photoId = "";
            if (imageSource != null) {
                String albumId = getAlbumId(publisher, messageTemplateService.getMessage(PHOTO_ALBUM_NAME), page);
                photoId = uploadPhoto(imageSource.getImageBytes(), publisher, albumId, page);
            }
            message = message == null ? "" : message;
            String url = MessageFormat.format(API_POST_COMPANY_MESSAGE_TEMPLATE, "-" + page.getPageId(), URLEncoder.encode(message, "UTF-8"), photoId, publisher.getAccessToken());
            String data = requestApiData(url);
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();

            if (!json.containsKey("response")) {
                if (json.containsKey("error") && json.getJSONObject("error").containsKey("error_code")) {
                    int errorCode = json.getJSONObject("error").getInt("error_code");
                    if (errorCode == 5) {
                        throw new AccessTokenExpiredException(publisher);
                    } else if (errorCode == 214 || errorCode == 7) {
                        throw new ResourceAccessDeniedException(SNResourceType.COMPANY_MESSAGE_POSTING, publisher);
                    }
                    throw new SNException(json.getJSONObject("error").getString("error_msg"));
                }
                throw new SNException("Unknown company message posting error");
            }
            return json.getJSONObject("response").getString("post_id");
        } catch (UnsupportedEncodingException ex) {
            throw new SNException(ex);
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

    private String requestApiData(String url) {
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

    private JSONObject requestApiDataJson(String url) {
        String data = requestApiData(url);
        JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
        if (json.containsKey("response")) {
            try {
                return json.getJSONArray("response").getJSONObject(0);
            } catch (JSONException ex) {
                try {
                    return json.getJSONObject("response");
                } catch (JSONException exc) {
                    throw new SNException(exc);
                }
            }
        } else if (json.containsKey("error")) {
            if (json.getJSONObject("error").containsKey("error_code")) {
                int errorCode = json.getJSONObject("error").getInt("error_code");
                if (errorCode == 5) {
                    throw new AccessTokenExpiredException(null);
                }
            }
            throw new SNException(json.getJSONObject("error").getString("error_msg"));
        }
        return null;
    }

    @Override
    public TotalsStatistics getStatisticsCompany(CompanySocialPage page, UserSocialAccount account) {
        final IncrementalTotals postsTotal = new IncrementalTotals();
        final IncrementalTotals commentsTotal = new IncrementalTotals();
        final IncrementalTotals likesTotal = new IncrementalTotals();
        final IncrementalTotals sharesTotal = new IncrementalTotals();

        long pager = 0l;
        Calendar date = new GregorianCalendar();

        while (pager < 10000) {
            String url = MessageFormat.format(API_STATISTICS_URL_TEMPLATE, "-" + page.getPageId(), pager, account.getAccessToken());
            String data = requestApiData(url);

            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("response")) {
                JSONArray arr = (JSONArray) json.get("response");
                int size = arr.size();
                for (int i = 1; i < size; i++) {
                    JSONObject item = arr.getJSONObject(i);

                    if (item.containsKey("date")) {
                        date.setTimeInMillis(item.getLong("date") * 1000);
                    }

                    postsTotal.increment(1, date);
                    commentsTotal.increment(getCount(item, "comments"), date);
                    likesTotal.increment(getCount(item, "likes"), date);
                    sharesTotal.increment(getCount(item, "reposts"), date);
                }
                if (size < 100) {
                    break;
                }
            } else {
                break;
            }
            pager += 100;
        }
        return new TotalsStatistics(new ArrayList<Parameter>() {

            {
                add(new Parameter(ParameterType.POSTS, postsTotal.getTotals()));
                add(new Parameter(ParameterType.LIKES, likesTotal.getTotals()));
                add(new Parameter(ParameterType.COMMENTS, commentsTotal.getTotals()));
                add(new Parameter(ParameterType.SHARES, sharesTotal.getTotals()));
            }
        });
    }

    @Override
    public Object getStatisticsUser(UserSocialAccount account) {
        long pager = 0l;
        Calendar date = new GregorianCalendar();
        List<SocialNetworkPost> result = new ArrayList<SocialNetworkPost>();
        while (pager < 10000) {
            String url = MessageFormat.format(API_STATISTICS_URL_TEMPLATE, account.getAccountId(), pager, account.getAccessToken());
            String data = requestApiData(url);

            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("response")) {
                JSONArray arr = (JSONArray) json.get("response");
                int size = arr.size();
                for (int i = 1; i < size; i++) {
                    JSONObject item = arr.getJSONObject(i);

                    if (item.containsKey("date")) {
                        date.setTimeInMillis(item.getLong("date") * 1000);
                    }

                    result.add(new SocialNetworkPost(new Date(),
                            item.getJSONObject("shares").getInt("count"),
                            item.getJSONObject("comments").getInt("count"),
                            item.getJSONObject("likes").getInt("count")));
                }
                if (size < 100) {
                    break;
                }

            } else {
                break;
            }
            pager += 100;
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

    @Override
    public Object getUserStatistics(UserSocialAccount account, List<SNPost> posts) {
        long pager = 0L;
        Calendar date = new GregorianCalendar();
        List<SocialNetworkPost> result = new ArrayList<SocialNetworkPost>();
        while (pager < 10000) {
            String url = MessageFormat.format(API_STATISTICS_URL_TEMPLATE, account.getAccountId(), pager, account.getAccessToken());
            String data = requestApiData(url);
            JSONObject json = (JSONObject) (new JSONTokener(data)).nextValue();
            if (json.containsKey("response")) {
                JSONArray arr = (JSONArray) json.get("response");
                int size = arr.size();
                for (int i = 1; i < size; i++) {
                    JSONObject item = arr.getJSONObject(i);
                    if (item.containsKey("date")) {
                        date.setTimeInMillis(item.getLong("date") * 1000);
                    }
                    if (findSNId(item.getString("id"), posts)) {
                        result.add(new SocialNetworkPost(new Date(),
                                item.getInt("reply_count"),
                                getCount(item, "comments"),
                                getCount(item, "likes")));
                    }

                }
                if (posts.isEmpty() || size < 100) {
                    break;
                }
            } else {
                break;
            }
            pager += 100;
        }
        return result;
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
}
