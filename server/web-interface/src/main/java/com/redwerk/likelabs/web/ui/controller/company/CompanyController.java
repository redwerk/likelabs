package com.redwerk.likelabs.web.ui.controller.company;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.ContentTypeFilter;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.domain.model.review.exception.NotAuthorizedReviewUpdateException;
import com.redwerk.likelabs.web.ui.controller.dto.ReviewFilterDto;
import org.apache.commons.lang.StringUtils;
import java.text.ParseException;
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

@Controller
@RequestMapping(value = "/company/{companyId}")
public class CompanyController {

    private static final int ITEMS_PER_PAGE_REVIEW = 8;
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
    public String dashboard(ModelMap model, @PathVariable Integer companyId) {

         Company company = companyService.getCompany(companyId);
         model.put("companyName", company.getName());
         model.put("page", "dashboard");
         model.put("cabinet", "company");
         return VIEW_COMPANY_DASHBOARD;
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public String reviewsCompany(ModelMap model, @PathVariable Long companyId) {

        Company company = companyService.getCompany(companyId);
        Report<Point> report = pointService.getPoints(companyId, Pager.ALL_RECORDS);
        model.addAttribute("points", report.getItems());
        model.addAttribute("company", company);
        model.addAttribute("count", report.getCount());
        model.addAttribute("items_per_page", ITEMS_PER_PAGE_REVIEW);
        model.addAttribute("page", "moderation");
        model.put("cabinet", "company");
        return VIEW_COMPANY_REVIEWS_LIST;
    }

    @RequestMapping(value = "/reviews/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewsCompanyData(@PathVariable Long companyId,
            @ModelAttribute ReviewFilterDto filter) {

        ModelMap response = new ModelMap();
        try {
            Report<Review> report = reviewService.getCompanyReviews(companyId, filter.getReviewStatus(), queryFilterBuilder(filter));
            Company company = companyService.getCompany(companyId);
            Set<Review> sampleReviews = company.getSampleReviews();
            List<Map> data = new ArrayList<Map>();
            for (Review review : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message", review.getMessage());
                map.put("name", review.getAuthor().getName());
                map.put("date", review.getCreatedDT());
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
            response.put("data", data);
            response.put("count", report.getCount());
        } catch (Exception e) {
            log.error(e, e);
            response.put("error", e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/reviews/{reviewId}/data/promo", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeSampleStatus(@PathVariable Long reviewId,
            @RequestParam("promo") Boolean sampleStatus) {

        ModelMap response = new ModelMap();
        try {
            Review review = reviewService.getReview(reviewId);
            reviewService.updateStatus(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),
                         reviewId, review.getStatus(), sampleStatus, false);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            response.put("error", messageTemplateService.getMessage("review.promo.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            response.put("error", messageTemplateService.getMessage("review.promo.change.failed"));
        }
        return response;
    }

    @RequestMapping(value = "/reviews/{reviewId}/data/status", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeReviewStatus(@PathVariable Long companyId, @PathVariable Long reviewId,
            @RequestParam("status") String status) {

        ModelMap response = new ModelMap();
        try {    
            Review review = reviewService.getReview(reviewId);
            Company company = companyService.getCompany(companyId);
            Set<Review> sampleReviews = company.getSampleReviews();
            reviewService.updateStatus(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),
                         reviewId, ReviewStatus.valueOf(status.toUpperCase(Locale.ENGLISH)), sampleReviews.contains(review), false);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            response.put("error", messageTemplateService.getMessage("review.status.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            response.put("error", messageTemplateService.getMessage("review.status.change.failed"));
        }
        return response;
    }

    @RequestMapping(value = "/reviews/{reviewId}/data/publish", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap publishReview(@PathVariable Long reviewId, @RequestParam("publish") Boolean publish) {

        ModelMap response = new ModelMap();
        try {    
            Review review = reviewService.getReview(reviewId);
            reviewService.updateStatus(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()),
                         reviewId, review.getStatus(), false, true);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            response.put("error", messageTemplateService.getMessage("review.public.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            response.put("error", messageTemplateService.getMessage("review.public.failed"));
        }
        return response;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    private ReviewQueryData queryFilterBuilder(ReviewFilterDto filter) throws ParseException {

       List<Long> pointIds = null;
       if (filter.getPoint() != null) {
           pointIds = new ArrayList<Long>();
           pointIds.add(filter.getPoint());
       }
       ContentTypeFilter contentType = null;
       if (StringUtils.isNotBlank(filter.getFeedType())) {
           contentType = ContentTypeFilter.valueOf(filter.getFeedType().toUpperCase(Locale.ENGLISH));
       }
       SortingRule sort = null;
       if (StringUtils.isNotBlank(filter.getSortBy())) {
           SortingCriteria sortingCriteria = SortingCriteria.valueOf(filter.getSortBy().toUpperCase(Locale.ENGLISH));
           SortingOrder sortingOrder = null;
           switch (sortingCriteria){
               case DATE:  sortingOrder = SortingOrder.DESCENDING;
                           break;
               case REVIEW_TYPE: sortingOrder = SortingOrder.DESCENDING;
                                 break;
               default: sortingOrder = SortingOrder.ASCENDING;
           }
           sort = new SortingRule(sortingCriteria, sortingOrder);
       }
       Pager pager = new Pager(filter.getPage() * ITEMS_PER_PAGE_REVIEW, ITEMS_PER_PAGE_REVIEW);
       return new ReviewQueryData(pointIds, filter.getFromDate(), filter.getToDate(),
                contentType, filter.getSampleStatus(), filter.getPublishingStatus(), pager, sort);
    }
}
