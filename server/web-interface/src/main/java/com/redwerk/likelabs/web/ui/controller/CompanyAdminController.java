package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.application.dto.user.UserData;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.*;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.web.ui.controller.dto.ProfileData;
import com.redwerk.likelabs.web.ui.validator.ProfileValidator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/companyadmin")
public class CompanyAdminController {

    private static final String VIEW_PROFILE = "companyadmin/profile";
    private static final String VIEW_COMPANY_LIST = "companyadmin/list";
    private static final String VIEW_REVIEW_LIST = "companyadmin/feed";
    private static final String PROFILE_REDIRECT_URL = "redirect:/companyadmin/profile";
    private static final int COMPANY_LIST_PAGE_SIZE = 10;
    private static final int REVIEW_LIST_PAGE_SIZE = 8;
    private static final String LOGO_LINK_TEMPLATE = "/company/{0}/logo";
    private static final String PHOTO_REVIEW_LINK_TEMPLATE = "/company/review/{0}/photo";
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profileGet(ModelMap model) {
        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("profile", new ProfileData(user.getPhone(), user.getPassword(), user.getEmail()));
        return VIEW_PROFILE;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profileSave(@ModelAttribute("profile") ProfileData profileData,
            @RequestParam(value = "oldUserId") long userId,
            BindingResult result) {
        new ProfileValidator().validate(profileData, result);
        if (!result.hasErrors()) {
            userService.updateUser(userId,
                    new UserData(profileData.getPhone(),
                    profileData.getPassword(),
                    profileData.getEmail(),
                    false,
                    false,
                    new HashSet<EventType>()));
            return PROFILE_REDIRECT_URL;
        } else {
            return VIEW_PROFILE;
        }
    }

    @RequestMapping(value = "/companies", method = RequestMethod.GET)
    public String companyList(ModelMap model) {
        return VIEW_COMPANY_LIST;
    }

    @RequestMapping(value = "/companies/data/{pageId:\\d}", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap companyListPaged(@PathVariable int pageId) {
        int offset = (pageId - 1) * COMPANY_LIST_PAGE_SIZE;
        int count = COMPANY_LIST_PAGE_SIZE;

        ModelMap modelMap = new ModelMap();

        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
        Report<CompanyReportItem> companiesResult = companyService.getCompanies(user.getId(), new Pager(offset, count));

        modelMap.put("companies", buildCompaniesJson(companiesResult.getItems()));
        modelMap.put("count", companiesResult.getCount());
        return modelMap;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String reviewList(ModelMap model) {

        return VIEW_REVIEW_LIST;
    }

    @RequestMapping(value = "/feed/data/{pageId:\\d}", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewListPaged(@PathVariable int pageId,
            @RequestParam(value = "status", defaultValue = "") String statusFilterParam,
            @RequestParam(value = "point", defaultValue = "") String pointFilterParam,
            @RequestParam(value = "startDate", defaultValue = "") String startDateParam,
            @RequestParam(value = "endDate", defaultValue = "") String endDateParam,
            @RequestParam(value = "content", defaultValue = "") String contentFilterParam,
            @RequestParam(value = "sortingCriteria", defaultValue = "") String sortingCriteriaParam,
            @RequestParam(value = "sortingOrder", defaultValue = "") String sortingOrderParam) {

        ModelMap modelMap = new ModelMap();

        ReviewStatus statusFilter = null;

        if (StringUtils.isNotEmpty(statusFilterParam)) {
            statusFilter = ReviewStatus.valueOf(statusFilterParam);
        }

        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());

        int offset = (pageId - 1) * REVIEW_LIST_PAGE_SIZE;
        int count = REVIEW_LIST_PAGE_SIZE;
        Pager pager = new Pager(offset, count);

        ReviewQueryData reviewQueryData = buildReviewQuery(pointFilterParam,
                contentFilterParam,
                startDateParam,
                endDateParam,
                sortingCriteriaParam,
                sortingOrderParam,
                pager);

        Report<Review> reviewsResult = reviewService.getCompanyReviews(user.getId(),
                statusFilter,
                reviewQueryData);

        modelMap.put("reviews", buildReviewsJson(reviewsResult.getItems()));
        modelMap.put("count", reviewsResult.getCount());
        return modelMap;

    }

    @RequestMapping(value = "/review/{reviewId:\\d}/status/", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeReviewStatus(@PathVariable long reviewId,
            @RequestParam(value = "status", defaultValue = "") String newStatusParam) {
        ModelMap modelMap = new ModelMap();
        try {
            User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
            ReviewStatus reviewStatus = ReviewStatus.valueOf(newStatusParam);
            reviewService.updateStatus(user.getId(), reviewId, reviewStatus, false, false);
            modelMap.put("stasus", "OK");
        } catch (Exception ex) {
            modelMap.put("stasus", "ERROR");
        }
        return modelMap;
    }

    @RequestMapping(value = "/review/{reviewId:\\d}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteReview(@PathVariable long reviewId) {
        ModelMap modelMap = new ModelMap();
        try {
            reviewService.deleteReview(reviewId);
            modelMap.put("stasus", "OK");
        } catch (Exception ex) {
            modelMap.put("stasus", "ERROR");
        }
        return modelMap;
    }

    private ReviewQueryData buildReviewQuery(String pointFilterParam,
            String contentFilterParam,
            String startDateParam,
            String endDateParam,
            String sortingCriteriaParam,
            String sortingOrderParam,
            Pager pager) {

        ReviewStatus statusFilter = null;
        List<Long> pointFilter = new ArrayList<Long>();
        Date startDate = null;
        Date endDate = null;
        ContentTypeFilter contentFilter = null;
        SortingCriteria sortingCriteria = null;
        SortingOrder sortingOrder = null;
        SortingRule sortingRule = null;

        try {
            if (StringUtils.isNotEmpty(startDateParam)) {
                startDate = DateFormat.getInstance().parse(startDateParam);
            }
            if (StringUtils.isNotEmpty(endDateParam)) {
                endDate = DateFormat.getInstance().parse(endDateParam);
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        if (StringUtils.isNotEmpty(pointFilterParam)) {
            pointFilter.add(Long.parseLong(pointFilterParam));
        }
        if (StringUtils.isNotEmpty(contentFilterParam)) {
            contentFilter = ContentTypeFilter.valueOf(contentFilterParam);
        }
        if (StringUtils.isNotEmpty(sortingCriteriaParam)) {
            sortingCriteria = SortingCriteria.valueOf(sortingCriteriaParam);
        }
        if (StringUtils.isNotEmpty(sortingOrderParam)) {
            sortingOrder = SortingOrder.valueOf(sortingOrderParam);
        }
        sortingRule = new SortingRule(sortingCriteria, sortingOrder);

        return new ReviewQueryData(pointFilter, startDate, endDate, contentFilter, false, false, pager, sortingRule);
    }

    private List<Map<String, String>> buildCompaniesJson(List<CompanyReportItem> companies) {
        List<Map<String, String>> result = new LinkedList<Map<String, String>>();
        for (CompanyReportItem company : companies) {
            Map<String, String> companyJson = new HashMap<String, String>();
            companyJson.put("name", company.getCompany() .getName());
            companyJson.put("id", company.getCompany().getId().toString());
            companyJson.put("points_count", new Integer(company.getPointsNumber()).toString());
            companyJson.put("reviews_count", new Integer(company.getPointsNumber()).toString());
            companyJson.put("logo", MessageFormat.format(LOGO_LINK_TEMPLATE, company.getCompany().getId()));
            result.add(companyJson);
        }
        return result;
    }

    private List<Map<String, String>> buildReviewsJson(List<Review> reviews) {
        List<Map<String, String>> result = new LinkedList<Map<String, String>>();
        for (Review review : reviews) {
            Map<String, String> reviewJson = new HashMap<String, String>();
            reviewJson.put("id", review.getId().toString());
            reviewJson.put("message", review.getMessage());
            reviewJson.put("name", review.getAuthor().getName());
            reviewJson.put("date", review.getCreatedDT().toString());
            reviewJson.put("status", review.getStatus().toString()); 
            reviewJson.put("published", review.isPublishedInCompanySN() ? "true":"false");
            reviewJson.put("point", review.getPoint().getAddress().getAddressLine1());
            reviewJson.put("photo", MessageFormat.format(PHOTO_REVIEW_LINK_TEMPLATE, String.valueOf(review.getId())));
            result.add(reviewJson);
        }
        return result;
    }
}
