package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.ContentTypeFilter;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import org.apache.commons.lang.StringUtils;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/company")
public class CompanyController {

    public static final int ITEMS_PER_PAGE_COMPANY = 3;
    public static final int ITEMS_PER_PAGE_POINT = 5;
    public static final int ITEMS_PER_PAGE_REVIEW = 8;
    private static final String LOGO_LINK_TEMPLATE = "/company/{0}/logo";
    private static final String PHOTO_REVIEW_LINK_TEMPLATE = "/company/review/{0}/photo";
    private static final String VIEW_COMPANY_LIST = "companies_list";
    private static final String VIEW_POINTS_LIST = "points_list";
    private static final String VIEW_REVIEWS_LIST = "review_list";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewService reviewService;

    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping(value = {"/", "", "/list"}, method = RequestMethod.GET)
    public String listCompany(ModelMap model) {

        model.addAttribute("count", companyService.getCompaniesCount());
        model.addAttribute("items_per_page", ITEMS_PER_PAGE_COMPANY);
        return VIEW_COMPANY_LIST;
    }

    @RequestMapping(value = "/list/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap listCompanyData(@RequestParam(value="page_number", defaultValue="0") Integer page) {

        ModelMap response = new ModelMap();
        try {
            Pager pager = new Pager(page * ITEMS_PER_PAGE_COMPANY, ITEMS_PER_PAGE_COMPANY);
            Report<CompanyReportItem> report = companyService.getCompanies(pager);
            List<Map> data = new ArrayList<Map>();
            for (CompanyReportItem reportItem : report.getItems()) {
                Map<String, String> map = new HashMap<String, String>();
                Company company = reportItem.getCompany();
                map.put("id", String.valueOf(company.getId()));
                map.put("logo", MessageFormat.format(LOGO_LINK_TEMPLATE, String.valueOf(company.getId())));
                map.put("name", company.getName());
                map.put("points", String.valueOf(reportItem.getPointsNumber()));
                map.put("comments", String.valueOf(reportItem.getReviewsNumber()));
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

    @RequestMapping(value = "/{companyId}/points", method = RequestMethod.GET)
    public String pointsCompany(ModelMap model, @PathVariable Integer companyId) {

        Company company = companyService.getCompany(companyId);
        model.addAttribute("company", company);
        model.addAttribute("items_per_page", ITEMS_PER_PAGE_POINT);
        model.addAttribute("count", pointService.getPoints(company.getId(), Pager.ALL_RECORDS).getCount());
        return VIEW_POINTS_LIST;
    }

    @RequestMapping(value = "/{companyId}/points/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap pointsCompanyData(@PathVariable Integer companyId,
            @RequestParam(value="page_number", defaultValue="0") Integer page) {

        ModelMap response = new ModelMap();
        try {
            Pager pager = new Pager(page * ITEMS_PER_PAGE_POINT, ITEMS_PER_PAGE_POINT);
            Report<Point> report = pointService.getPoints(companyId, pager);
            List<Map> data = new ArrayList<Map>();
            for (Point point : report.getItems()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", String.valueOf(point.getId()));
                map.put("address", point.getAddress().getAddressLine1());
                map.put("phone", point.getPhone());
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

    @RequestMapping(value = "/{companyId}/reviews", method = RequestMethod.GET)
    public String reviewsCompany(ModelMap model, @PathVariable Integer companyId) {

        Company company = companyService.getCompany(companyId);
        Report<Point> report = pointService.getPoints(companyId, Pager.ALL_RECORDS);
        model.addAttribute("points", report.getItems());
        model.addAttribute("company", company);
        model.addAttribute("count", report.getCount());
        model.addAttribute("items_per_page", ITEMS_PER_PAGE_REVIEW);
        return VIEW_REVIEWS_LIST;
    }

    @RequestMapping(value = "/{companyId}/reviews/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewsCompanyData(@PathVariable Integer companyId,
            @RequestParam("point") Long point,
            @RequestParam("feed_type") String feedType,
            @RequestParam("date_to") Date toDate,
            @RequestParam("date_from") Date fromDate,
            @RequestParam("sort_by") String sortBy,
            @RequestParam(value="page_number", defaultValue="0") Integer page) {

        ModelMap response = new ModelMap();
        try {
            ReviewQueryData query = queryFilterBuilder(point, feedType, toDate, fromDate, sortBy, page);
            Report<Review> report = reviewService.getPublicReviews(companyId, query);
            List<Map> data = new ArrayList<Map>();
            for (Review review : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (query.getType() != ContentTypeFilter.CONTAINS_TEXT) {
                    map.put("photo", MessageFormat.format(PHOTO_REVIEW_LINK_TEMPLATE, String.valueOf(review.getId())));
                }
                if (query.getType() != ContentTypeFilter.CONTAINS_PHOTO) {
                    map.put("message", review.getMessage());
                }
                map.put("name", review.getAuthor().getName());
                map.put("date", review.getCreatedDT());
                map.put("point", review.getPoint().getAddress().getAddressLine1());
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

    @RequestMapping(value = "/{companyId}/logo", method = RequestMethod.GET)
    public void getlogo(@PathVariable Integer companyId,
            HttpServletResponse response) {

        ServletOutputStream out = null;
        try {
            Company company = companyService.getCompany(companyId);
            if (company == null || company.getLogo() ==null) return;
            byte[] logo = company.getLogo();
            response.setContentType("image");
            out = response.getOutputStream();
            out.write(logo);
            out.close();
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    @RequestMapping(value = "/review/{reviewId}/photo", method = RequestMethod.GET)
    public void getReviewPhoto(@PathVariable Integer reviewId,
            HttpServletResponse response) {

        ServletOutputStream out = null;
        try {
            Review review = reviewService.getReview(reviewId);
            if (review == null || review.getPhoto() == null) return;
            byte[] photo = review.getPhoto().getImage();
            response.setContentType("image");
            out = response.getOutputStream();
            out.write(photo);
            out.close();
        } catch (IOException e) {
            log.error(e, e);
        }
    }

    private ReviewQueryData queryFilterBuilder(Long point, String feedType, Date toDate,
                                Date fromDate, String sortBy, Integer page) throws ParseException {
        
       List<Long> pointIds = null;
       if (point != null) {
           pointIds = new ArrayList<Long>();
           pointIds.add(point);
       }
       ContentTypeFilter contentType = null;
       if (StringUtils.isNotBlank(feedType)) {
           contentType = ContentTypeFilter.valueOf(feedType.toUpperCase(Locale.ENGLISH));
       }
       SortingRule sort = null;
       if (StringUtils.isNotBlank(sortBy)) {
           SortingCriteria sortingCriteria = SortingCriteria.valueOf(sortBy.toUpperCase(Locale.ENGLISH));
           sort = new SortingRule(sortingCriteria,
                    (sortingCriteria == SortingCriteria.DATE) ? SortingOrder.DESCENDING : SortingOrder.ASCENDING);
       }
       Pager pager = new Pager(page * ITEMS_PER_PAGE_REVIEW, ITEMS_PER_PAGE_REVIEW);
       return new ReviewQueryData(pointIds, fromDate, toDate, contentType, pager, sort);
    }


    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
