package com.redwerk.likelabs.web.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.web.rest.dto.exposable.Credentials;


public class TabletResource {
    
    private final CompanyService companyService;

    private final ReviewService reviewService;

    private final TabletService tabletService;
    
    private final long tabletId;

    
    public TabletResource(long tabletId, CompanyService companyService, TabletService tabletService, ReviewService reviewService) {
        this.tabletId = tabletId;
        this.companyService = companyService;
        this.tabletService = tabletService;
        this.reviewService = reviewService;
    }

    @Path("settings")
    public TabletSettingsResource getSettings() {
        return new TabletSettingsResource(tabletId, companyService, reviewService);
    }  

    @Path("reviews")
    public ReviewsResource getReviews() {
        return new ReviewsResource(tabletId, reviewService);
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.APPLICATION_XML)
    public void logout(Credentials credentials) {
        if(!tabletService.canLogout(tabletId, credentials.getPassword())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
  }
