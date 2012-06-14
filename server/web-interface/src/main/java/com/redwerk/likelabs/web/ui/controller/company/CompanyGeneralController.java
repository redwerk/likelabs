package com.redwerk.likelabs.web.ui.controller.company;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.domain.model.review.exception.NotAuthorizedReviewUpdateException;
import com.redwerk.likelabs.web.ui.dto.ReviewFilterDto;
import com.redwerk.likelabs.web.ui.utils.JsonResponseBuilder;
import com.redwerk.likelabs.web.ui.utils.QueryFilterBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * Secure on Controller uses {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 * All methods for mapping must have parameter companyId
 */
@PreAuthorize("@decisionAccess.permissionCompany(principal, #companyId)")
@Controller
@RequestMapping(value = "/company/{companyId}")
public class CompanyGeneralController {

    private static final String VIEW_COMPANY_REVIEWS_LIST = "company/company_review_list";
    private static final String VIEW_COMPANY_DASHBOARD = "company/dashboard";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    public String cabinet(ModelMap model, @PathVariable Long companyId) {

         return "redirect:/company/"+ companyId + "/dashboard";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(ModelMap model, @PathVariable Long companyId) {

         Company company = companyService.getCompany(companyId);
         model.put("companyName", company.getName());
         model.put("page", "dashboard");
         return VIEW_COMPANY_DASHBOARD;
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public String reviewsCompany(ModelMap model, @PathVariable Long companyId) {

        Company company = companyService.getCompany(companyId);
        Report<Point> report = pointService.getPoints(companyId, Pager.ALL_RECORDS);
        model.addAttribute("points", report.getItems());
        model.addAttribute("company", company);
        model.addAttribute("count", report.getCount());
        model.addAttribute("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_REVIEW);
        model.addAttribute("page", "moderation");
        return VIEW_COMPANY_REVIEWS_LIST;
    }

    @RequestMapping(value = "/reviews/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewsCompanyData(@PathVariable Long companyId,
            @ModelAttribute ReviewFilterDto filter) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            Report<Review> report = reviewService.getCompanyReviews(companyId, filter.getReviewStatus(), QueryFilterBuilder.buildReviewQuery(filter));
            Company company = companyService.getCompany(companyId);
            Set<Review> sampleReviews = company.getSampleReviews();
            List<Map> data = new ArrayList<Map>();
            for (Review review : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message", review.getMessage());
                map.put("name", review.getAuthor().getName());
                map.put("date", review.getCreatedDT());
                map.put("containsPhoto", review.getPhoto() != null);
                if (review.getPoint().getAddress() != null) {
                    map.put("point", review.getPoint().getAddress().getAddressLine1());
                }
                map.put("id", review.getId());
                if (sampleReviews.contains(review)) {
                    map.put("promo", true);
                } else {
                    map.put("promo", false);
                }
                map.put("published",review.isPublishedInCompanySN());
                map.put("status", review.getStatus().toString().toLowerCase(Locale.ENGLISH));
                data.add(map);
            }
            resBuilder.setData(data);
            resBuilder.addCustomFieldData("count", report.getCount());
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/reviews/{reviewId}/data/promo", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeSampleStatus(@PathVariable Long companyId, @PathVariable Long reviewId,
            @RequestParam("promo") Boolean sampleStatus) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            Review review = reviewService.getReview(reviewId);
            reviewService.updateStatus(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),
                         reviewId, review.getStatus(), sampleStatus, false);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.promo.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.promo.change.failed"));
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/reviews/{reviewId}/data/status", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeReviewStatus(@PathVariable Long companyId, @PathVariable Long reviewId,
            @RequestParam("status") String status) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {    
            Review review = reviewService.getReview(reviewId);
            Company company = companyService.getCompany(companyId);
            Set<Review> sampleReviews = company.getSampleReviews();
            reviewService.updateStatus(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),
                         reviewId, ReviewStatus.valueOf(status.toUpperCase(Locale.ENGLISH)), sampleReviews.contains(review), false);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.status.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.status.change.failed"));
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/reviews/{reviewId}/data/publish", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap publishReview(@PathVariable Long companyId, @PathVariable Long reviewId, @RequestParam("publish") Boolean publish) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {    
            Review review = reviewService.getReview(reviewId);
            reviewService.updateStatus(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),
                         reviewId, review.getStatus(), false, true);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.public.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.public.failed"));
        }
        return resBuilder.getModelResponse();
    }

    @PreAuthorize("permitAll")
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
