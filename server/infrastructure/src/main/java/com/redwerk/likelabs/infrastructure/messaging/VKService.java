package com.redwerk.likelabs.infrastructure.messaging;

import java.net.URLEncoder;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.log4j.Logger;

public class VKService extends SocialNetworkService {
    
    private static final Logger log = Logger.getLogger(VKService.class);
    
    private static final String VK_API_URL = "https://api.vk.com/method";
    
    public VKService(String profileId, String access_token) {
        super(profileId, access_token, VK_API_URL);
    }

    public String getUserName() throws Exception {
        String data = getData("users.get?uid=" + profileId);
        JSONObject dataJson = (JSONObject) (new JSONTokener(data)).nextValue();
        dataJson = dataJson.getJSONArray("response").getJSONObject(0);
        return dataJson.getString("first_name") + " " + dataJson.getString("last_name");
    }

    public String getEmail() throws Exception {
        // We cannot get email with VK API
        return null;
    }

    /**
     * profileId for Company must be negative
     */
    public String publishMessage(String message) throws Exception {
        return getData("wall.post?owner_id=" + profileId + "&message=" + URLEncoder.encode(message, "UTF-8"));
    }

    public boolean checkIsAdmin(String companyId) throws Exception {
        String data = getData("groups.getById?gid=" + companyId);
        JSONObject json = ((JSONObject) (new JSONTokener(data)).nextValue()).getJSONArray("response").getJSONObject(0);
        return "1".equals(json.getString("is_admin"));
    }
}
