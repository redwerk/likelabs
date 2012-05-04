package com.redwerk.likelabs.infrastructure.messaging;

import com.redwerk.likelabs.application.SocialNetworkInterface;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

public abstract class SocialNetworkService implements SocialNetworkInterface {
    
    protected String apiUrl;
    
    public String access_token;
    
    public String profileId;
    
    public SocialNetworkService(String profileId, String access_token, String apiUrl) {
        this.profileId = profileId;
        this.access_token = access_token;
        this.apiUrl = apiUrl;

    }
    
    protected String getData(String url) throws Exception {
        if (StringUtils.isEmpty(url)) throw new Exception();
        
        if (!url.startsWith("http")) url = apiUrl + (url.startsWith("/")? url : "/" + url);
        
        if (StringUtils.isNotBlank(access_token)) url += url.contains("?")? "&access_token=" + access_token : "?access_token=" + access_token;

        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        client.executeMethod(getMethod);
        return getMethod.getResponseBodyAsString();
    }

    protected String postData(String url, NameValuePair[] parameters) throws Exception {
        if (StringUtils.isEmpty(url)) throw new Exception();
        
        if (!url.startsWith("http")) url = apiUrl + (url.startsWith("/")? url : "/" + url);
        
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameters(parameters);
        postMethod.addParameter("access_token", access_token);
        client.executeMethod(postMethod);
        return postMethod.getResponseBodyAsString();
    }
}
