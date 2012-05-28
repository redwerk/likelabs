package com.redwerk.likelabs.web.rest.resource.mapping;

import java.util.Map;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.common.collect.Maps;


public class LinkBuilder {

    public static class Url {
        public static final String TABLETS_BASE =          "/tablets";
        public static final String TABLET_SETTINGS  =      TABLETS_BASE + "/{tabletId}/settings";
        public static final String SAMPLE_REVIEW_IMAGE =   TABLETS_BASE + "/{tabletId}/settings/sample-reviews/{reviewId}/image";
        public static final String LOGO =                  TABLETS_BASE + "/{tabletId}/settings/logo";
        public static final String REVIEWS =               TABLETS_BASE + "/{tabletId}/reviews";  
        public static final String LOGOUT =                TABLETS_BASE + "/{tabletId}/logout";
        public static final String LOGIN =                 TABLETS_BASE + "/login" ;
    }
    
    public static class Param {        
        public static final String TABLET_ID = "tabletId";
        public static final String REVIEW_ID = "reviewId";
    }
    
    private UriBuilder uriBuilder;
    private Map<String, Object> values = Maps.newHashMap();
    
    
    public LinkBuilder(UriInfo uriInfo) {
        uriBuilder = uriInfo.getBaseUriBuilder();
    }
           
    public LinkBuilder tabletId(long tabletId) {
        setParam(Param.TABLET_ID, tabletId);
        return this;
    }
    
    public LinkBuilder reviewId(long reviewId) {
        setParam(Param.REVIEW_ID, reviewId);
        return this;
    }
    
    public String buildSampleReviewImageUrl() {
        return buildFor(Url.SAMPLE_REVIEW_IMAGE);
    }
    
    public String buildLogoImageUrl() {
        return buildFor(Url.LOGO);
    }  
    
    public LinkBuilder setParam(String variable, Object value) {
        values.put(variable, value);
        return this;
    }
    
    public String buildFor(String path) {
        return uriBuilder.clone().path(path).buildFromMap(values).toString();
    }
}
