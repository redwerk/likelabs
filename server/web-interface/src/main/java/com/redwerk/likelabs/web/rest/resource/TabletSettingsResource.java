package com.redwerk.likelabs.web.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.tablet.exception.TabletNotFoundException;
import com.redwerk.likelabs.web.rest.dto.exposable.TabletSettings;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;



public class TabletSettingsResource {
    
    private final long tabletId;
    
    private final CompanyService companyService;
    
    private final ReviewService reviewService;
    

    public TabletSettingsResource(long tabletId, CompanyService companyService, ReviewService reviewService) {
        this.tabletId = tabletId;
        this.companyService = companyService;
        this.reviewService = reviewService;
    }
    
    @GET
    public TabletSettings getSettings(@Context UriInfo uriInfo) {
        Company company = null;
        try {
            company = companyService.getCompanyForTablet(tabletId);
        } catch(TabletNotFoundException ex) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        TabletSettings settings = new TabletSettings.Builder().company(company)
            .urlBuilder(new LinkBuilder(uriInfo).tabletId(tabletId))
            .build();
        return settings;
    }
    
    @Path("logo")
    public ImageResource getCompanyLogo() {
        return new ImageResource(companyService.getCompanyForTablet(tabletId).getLogo());
    }

    @Path("sample-reviews")
    public ReviewsResource getSampleReviews() {
        return new ReviewsResource(tabletId, reviewService);
    }
}
