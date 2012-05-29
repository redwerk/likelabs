package com.redwerk.likelabs.web.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.web.rest.dto.exposable.Credentials;
import com.redwerk.likelabs.web.rest.dto.exposable.TabletExposable;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;


@Path(LinkBuilder.Url.TABLETS_BASE)
@Component
@Scope("singleton")
public class TabletsResource {
    
    @Autowired
    private CompanyService companyService;

    @Autowired  
    private ReviewService reviewService;

    @Autowired
    private TabletService tabletService;


    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_XML)
    public TabletExposable login(Credentials credentials) {
        Tablet tablet = tabletService.getTablet(credentials.getLogin(), credentials.getPassword());
        if(tablet == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return new TabletExposable(tablet);
    }
    
    @Path("{tabletId}")
    public TabletResource getTablet(@PathParam(LinkBuilder.Param.TABLET_ID) long tabletId) {
        return new TabletResource(tabletId, companyService, tabletService, reviewService);
    }
}
