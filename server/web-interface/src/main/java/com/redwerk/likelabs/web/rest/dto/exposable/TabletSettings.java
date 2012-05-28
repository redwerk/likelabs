package com.redwerk.likelabs.web.rest.dto.exposable;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Lists;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;


@XmlRootElement(name = "tabletSettings")
@XmlAccessorType(XmlAccessType.FIELD)
public class TabletSettings {
	
	private final String companyName;
	
	private final String companyLogoUrl;
	
	@XmlElementWrapper(name="sampleReviews")
	@XmlElement(name="sampleReview")
	private List<SampleReview> sampleReviews;

	
	public TabletSettings(String companyName, String companyLogoUrl, List<SampleReview> sampleReviews) {
	    this.companyName = companyName;
	    this.companyLogoUrl = companyLogoUrl;
	    this.sampleReviews = sampleReviews;
	}
	
    /** Used internally by JAXB for object instantiation */
    @SuppressWarnings("unused")
    private TabletSettings () {
        this(null, null, null);        
    }
	
	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyLogoUrl() {
		return companyLogoUrl;
	}
	
	public List<SampleReview> getSampleReviews() {
		return sampleReviews;
	}
	
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("companyName", companyName)
                .append("companyLogoUrl", companyLogoUrl)
                .append("promoReviews", sampleReviews)
                .toString();
    }
    
    
    public static class Builder {

        private Company company;

        private LinkBuilder urlBuilder;


        public Builder urlBuilder(LinkBuilder urlBuilder) {
            this.urlBuilder = urlBuilder;
            return this;
        }

        public Builder company(Company company) {
            this.company = company;
            return this;
        }

        public TabletSettings build() {           
            List<SampleReview> sampleReviews = Lists.newArrayList();
            SampleReview.Builder sampleReviewBuilder = new SampleReview.Builder().urlBuilder(urlBuilder);
            for(Review review : company.getSampleReviews()) {
                sampleReviews.add(sampleReviewBuilder.review(review).build());
            }     
            return new TabletSettings(company.getName(), urlBuilder.buildLogoImageUrl(), sampleReviews);   
        }
    }
}
