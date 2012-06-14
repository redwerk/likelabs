package com.redwerk.likelabs.web.ui.controller.companyadmin;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.*;
import com.redwerk.likelabs.domain.model.user.User;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * Secure on Controller uses {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 * All methods for mapping must have parameter adminId
 */
@PreAuthorize("@decisionAccess.permissionCompanyAdmin(principal, #adminId)")
@Controller
@RequestMapping(value = "/companyadmin/{adminId}")
public class CompanyAdminGeneralController {

    private static final String VIEW_COMPANY_LIST = "companyadmin/list";
    private static final String VIEW_REVIEW_LIST = "companyadmin/feed";
    private static final int COMPANY_LIST_PAGE_SIZE = 10;
    private static final int REVIEW_LIST_PAGE_SIZE = 8;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final String LOGO_LINK_TEMPLATE = "/public/{0}/logo";
    private static final String PHOTO_REVIEW_LINK_TEMPLATE = "/public/review/{0}/photo";
    
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private PointService pointService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public String cabinet(ModelMap model, @PathVariable Long adminId) {

         return "redirect:/companyadmin/" + adminId + "/companies";
    }

    @RequestMapping(value = "/companies", method = RequestMethod.GET)
    public String companyList(ModelMap model, @PathVariable Long adminId) {

        model.addAttribute("items_per_page" ,COMPANY_LIST_PAGE_SIZE);
        model.addAttribute("page", "companies");
        model.addAttribute("cabinet", "company_admin");
        return VIEW_COMPANY_LIST;
    }

    @RequestMapping(value = "/companies/data/{pageId:\\d}", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap companyListPaged(@PathVariable Long adminId , @PathVariable int pageId ) {

        int offset = pageId * COMPANY_LIST_PAGE_SIZE;
        int count = COMPANY_LIST_PAGE_SIZE;

        ModelMap modelMap = new ModelMap();

        User user = userService.getUser(adminId);
        Report<CompanyReportItem> companiesResult = companyService.getCompaniesForAdmin(user.getId(), new Pager(offset, count));
        modelMap.put("companies", buildCompaniesJson(companiesResult.getItems()));
        modelMap.put("count", companiesResult.getCount());
        return modelMap;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String reviewList(ModelMap model, @PathVariable Long adminId) {

        User user = userService.getUser(adminId);
        Map<String, List<Point>> companiesPointsMap = new HashMap<String, List<Point>>();
        List<CompanyReportItem> companies = companyService.getCompaniesForAdmin(user.getId(), Pager.ALL_RECORDS).getItems();
        for (CompanyReportItem company : companies) {
            List<Point> points = pointService.getPoints(company.getCompany().getId(), Pager.ALL_RECORDS).getItems();
            companiesPointsMap.put(company.getCompany().getName(), points);
        }
        model.addAttribute("compniesPointsMap", companiesPointsMap);
        model.addAttribute("items_per_page", REVIEW_LIST_PAGE_SIZE);
        model.addAttribute("page", "moderation");
        model.addAttribute("cabinet", "company_admin");
        return VIEW_REVIEW_LIST;
    }

    @RequestMapping(value = "/feed/data/{pageId:\\d}", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewListPaged(@PathVariable Long adminId, @PathVariable int pageId,
            @RequestParam(value = "status", defaultValue = "") String statusFilterParam,
            @RequestParam(value = "point", defaultValue = "") String pointFilterParam,
            @RequestParam(value = "startDate", defaultValue = "") String startDateParam,
            @RequestParam(value = "endDate", defaultValue = "") String endDateParam,
            @RequestParam(value = "contentType", defaultValue = "") String contentFilterParam,
            @RequestParam(value = "sortingCriteria", defaultValue = "") String sortingCriteriaParam) {

        ModelMap modelMap = new ModelMap();
        
        Boolean sampleFilter = null;
        Boolean publishedFilter = null;
        ReviewStatus statusFilter = null;
        if (StringUtils.isNotBlank(statusFilterParam)) {
            if (statusFilterParam.equals("published")) {
                publishedFilter = true;
            } else if (statusFilterParam.equals("promo")) {
                sampleFilter = true;
            } else {
                statusFilter = ReviewStatus.valueOf(statusFilterParam);
            }
        }

        User user = userService.getUser(adminId);
        int offset = pageId * REVIEW_LIST_PAGE_SIZE;
        int count = REVIEW_LIST_PAGE_SIZE;
        Pager pager = new Pager(offset, count);

        ReviewQueryData reviewQueryData = buildReviewQuery(pointFilterParam,
                contentFilterParam,
                startDateParam,
                endDateParam,
                sortingCriteriaParam,
                sampleFilter,
                publishedFilter,
                pager);

        Report<Review> reviewsResult = reviewService.getModeratorReviews(user.getId(),
                statusFilter,
                reviewQueryData);
        
        Set<Review> sampleReviews = new HashSet<Review>();
        List<CompanyReportItem> companies = companyService.getCompaniesForAdmin(user.getId(), Pager.ALL_RECORDS).getItems();
        for (CompanyReportItem company : companies) {
            Company fullCompany = companyService.getCompany(company.getCompany().getId());
            sampleReviews.addAll(fullCompany.getSampleReviews());
        }

        modelMap.put("reviews", buildReviewsJson(reviewsResult.getItems(), sampleReviews));
        modelMap.put("count", reviewsResult.getCount());
        return modelMap;

    }

    private ReviewQueryData buildReviewQuery(String pointFilterParam,
            String contentFilterParam,
            String startDateParam,
            String endDateParam,
            String sortingCriteriaParam,
            Boolean sampleFilter,
            Boolean publishedFilter,
            Pager pager) {

        ReviewStatus statusFilter = null;
        List<Long> pointFilter = new ArrayList<Long>();
        Date startDate = null;
        Date endDate = null;
        ContentTypeFilter contentFilter = null;
        SortingCriteria sortingCriteria = null;
        SortingOrder sortingOrder = SortingOrder.ASCENDING;
        SortingRule sortingRule = null;

        try {
            if (StringUtils.isNotEmpty(startDateParam)) {
                startDate = DATE_FORMAT.parse(startDateParam);
            }
            if (StringUtils.isNotEmpty(endDateParam)) {
                endDate = DATE_FORMAT.parse(endDateParam);
            }
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }

        if (StringUtils.isNotEmpty(pointFilterParam)) {
            for (String pointId : StringUtils.split(pointFilterParam, ",")) {
                pointFilter.add(Long.parseLong(pointId));
            }
        }
        if (StringUtils.isNotEmpty(contentFilterParam)) {
            contentFilter = ContentTypeFilter.valueOf(contentFilterParam);
        }
        if (StringUtils.isNotEmpty(sortingCriteriaParam)) {
            sortingCriteria = SortingCriteria.valueOf(sortingCriteriaParam);
            if (sortingCriteria == SortingCriteria.DATE ||
                    sortingCriteria == SortingCriteria.REVIEW_TYPE) {
                sortingOrder = SortingOrder.DESCENDING;
            }
        }
        sortingRule = new SortingRule(sortingCriteria, sortingOrder);

        return new ReviewQueryData(pointFilter, startDate, endDate, contentFilter, sampleFilter, publishedFilter, pager, sortingRule);
    }

    private List<Map<String, Object>> buildCompaniesJson(List<CompanyReportItem> companies) {
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        for (CompanyReportItem company : companies) {
            Map<String, Object> companyJson = new HashMap<String, Object>();
            companyJson.put("name", company.getCompany() .getName());
            companyJson.put("id", company.getCompany().getId().toString());
            companyJson.put("points_count", new Integer(company.getPointsNumber()).toString());
            companyJson.put("reviews_count", new Integer(company.getPointsNumber()).toString());
            companyJson.put("logo", MessageFormat.format(LOGO_LINK_TEMPLATE, company.getCompany().getId()));
            result.add(companyJson);
        }
        return result;
    }

    private List<Map<String, Object>> buildReviewsJson(List<Review> reviews, Set<Review> sampleReviews) {
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        for (Review review : reviews) {
            Map<String, Object> reviewJson = new HashMap<String, Object>();
            reviewJson.put("id", review.getId().toString());
            reviewJson.put("companyId", review.getPoint().getCompany().getId().toString()); // todo: ???
            reviewJson.put("message", review.getMessage());
            reviewJson.put("name", review.getAuthor().getName());
            reviewJson.put("date", DATE_FORMAT.format(review.getCreatedDT()));
            reviewJson.put("status", review.getStatus().toString()); 
            reviewJson.put("published", review.isPublishedInCompanySN());
            reviewJson.put("promo", sampleReviews.contains(review) ? true : false);
            reviewJson.put("point", review.getPoint().getAddress().getAddressLine1());
            reviewJson.put("containsPhoto", review.getPhoto() != null);
            reviewJson.put("photo", MessageFormat.format(PHOTO_REVIEW_LINK_TEMPLATE, String.valueOf(review.getId())));
            result.add(reviewJson);
        }
        return result;
    }
}
