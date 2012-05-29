package com.redwerk.likelabs.web.rest.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.domain.model.tablet.exception.TabletNotFoundException;
import com.redwerk.likelabs.web.rest.dto.exposable.LinkElement;
import com.redwerk.likelabs.web.rest.dto.exposable.ReferenceData;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;
import com.redwerk.likelabs.web.rest.security.TabletSecurityFilter;


@Path("/")
@Component
@Scope("singleton")
public class ReferenceResource {

    @Autowired
    private TabletService tabletService;


    private static final String TABLET_ID_PLACEHOLDER =  "TABLET_ID"; 


    @GET
    public ReferenceData getReferenceData(@Context UriInfo uriInfo, 
            @HeaderParam(TabletSecurityFilter.HEADER_TABLET_API_KEY) String apiKey) {

        LinkBuilder linkBuilder = new LinkBuilder(uriInfo);
        List<LinkElement> links = Lists.newArrayList();        
        if(apiKey == null) {
            linkBuilder.setParam(LinkBuilder.Param.TABLET_ID, TABLET_ID_PLACEHOLDER);
            links.add(new LinkElement(linkBuilder.buildFor(LinkBuilder.Url.LOGIN)));
        } else {
            Object tabletId;
            try {
                tabletId = tabletService.getTabletId(apiKey);
            } catch (TabletNotFoundException ex) {
                tabletId = TABLET_ID_PLACEHOLDER;
            }
            linkBuilder.setParam(LinkBuilder.Param.TABLET_ID, tabletId);
            links.add(new LinkElement(linkBuilder.buildFor(LinkBuilder.Url.TABLET_SETTINGS)));
            links.add(new LinkElement(linkBuilder.buildFor(LinkBuilder.Url.REVIEWS)));
            links.add(new LinkElement(linkBuilder.buildFor(LinkBuilder.Url.LOGOUT)));            
        }     
        return new ReferenceData(links);
    }
}
