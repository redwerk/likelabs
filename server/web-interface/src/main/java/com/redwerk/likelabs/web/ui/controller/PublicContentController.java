package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PhotoService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.web.ui.dto.ReviewFilterDto;
import com.redwerk.likelabs.web.ui.utils.QueryFilterBuilder;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
@RequestMapping(value = "/public")
public class PublicContentController {

    public static final int ITEMS_PER_PAGE_COMPANY = 10;
    public static final int ITEMS_PER_PAGE_POINT = 10;
    public static final int ITEMS_PER_PAGE_REVIEW = 8;
    private static final String VIEW_COMPANY_LIST = "companies_list";
    private static final String VIEW_POINTS_LIST = "points_list";
    private static final String VIEW_REVIEWS_LIST = "review_list";
    private static final String VIEW_REVIEW_DETAILS = "review_details";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PhotoService photoService;
    
    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping(value = {"/", "", "/list"}, method = RequestMethod.GET)
    public String listCompany(ModelMap model) {

        model.addAttribute("count", companyService.getCompaniesCount());
        model.addAttribute("items_per_page", ITEMS_PER_PAGE_COMPANY);
        model.addAttribute("page", "companies");
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
        model.addAttribute("page", "companies");
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
    public String reviewsPublic(ModelMap model, @PathVariable Integer companyId) {

        Company company = companyService.getCompany(companyId);
        Report<Point> report = pointService.getPoints(companyId, Pager.ALL_RECORDS);
        model.addAttribute("points", report.getItems());
        model.addAttribute("company", company);
        model.addAttribute("count", report.getCount());
        model.addAttribute("items_per_page", ITEMS_PER_PAGE_REVIEW);
        model.addAttribute("page", "feed");
        model.put("cabinet", "company");
        return VIEW_REVIEWS_LIST;
    }

    @RequestMapping(value = "/{companyId}/reviews/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewsPublicData(@PathVariable Integer companyId,
                  @ModelAttribute ReviewFilterDto filter) {

        ModelMap response = new ModelMap();
        try {
            Report<Review> report = reviewService.getPublicReviews(companyId, QueryFilterBuilder.buildReviewQuery(filter));
            List<Map> data = new ArrayList<Map>();
            for (Review review : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message", review.getMessage());
                map.put("name", review.getAuthor().getName());
                map.put("date", review.getCreatedDT());
                map.put("point", review.getPoint().getAddress().getAddressLine1());
                map.put("containsPhoto", review.getPhoto() != null);
                map.put("id", review.getId());
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

    @RequestMapping(value = "/review/{reviewId}", method = RequestMethod.GET)
    public String getReviewDetails(ModelMap model, @PathVariable Long reviewId) {

        Review review = reviewService.getReview(reviewId);
        model.put("isAllowed", review.getStatus() == ReviewStatus.APPROVED);
        model.put("review",review);
        return VIEW_REVIEW_DETAILS;
    }

    @RequestMapping(value = "/{companyId}/logo", method = RequestMethod.GET)
    public void getlogo(HttpSession session, @PathVariable Integer companyId,
            HttpServletResponse response) {

        ServletOutputStream out = null;
        try {
            Company company = companyService.getCompany(companyId);
            if (company == null || company.getLogo() == null) return;
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

    @RequestMapping(value = "/photo/{photoId}", method = RequestMethod.GET)
    public void getPhoto(@PathVariable Integer photoId,
            HttpServletResponse response) {

        ServletOutputStream out = null;
        try {
            Photo photo = photoService.getPhoto(photoId);
            if (photo == null) return;
            response.setContentType("image");
            out = response.getOutputStream();
            out.write(photo.getImage());
            out.close();
        } catch (IOException e) {
            log.error(e, e);
        }
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
