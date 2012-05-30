package com.redwerk.likelabs.web.ui.controller.user;

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
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.web.ui.controller.dto.ProfileData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final String VIEW_USER_PROFILE = "user/profile";
    
    private static final String VIEW_USER_PHOTOS = "user/photos";
    
    private static final String VIEW_USER_SETTINGS = "user/settings";
    
    private static final String VIEW_REVIEWS_LIST = "user/review_list";
    
    private static final String VIEW_USER_CABINET = "user";
    
    public static final int ITEMS_PER_PAGE_REVIEW = 8;
    
    @Autowired
    private CompanyService companyService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping("/profile")
    public String profile(ModelMap model) {
        User user = userService.getUser(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.put("profile", new ProfileData(user.getPhone(), "", user.getEmail()));
        model.put("page", "profile");
        model.put("cabinet", "user");
        return VIEW_USER_PROFILE;
    }

    @RequestMapping("/photos")
    public String photos(ModelMap model) {
        long companyId = 1;
        Company company = companyService.getCompany(companyId);
        Report<Point> report = pointService.getPoints(companyId, Pager.ALL_RECORDS);
        model.put("points", report.getItems());
        model.put("company", company);
        model.put("count", report.getCount());
        model.put("items_per_page", 8);
        model.put("cabinet", "user");
        model.put("page", "my_photos");
        return VIEW_USER_PHOTOS;
    }

    @RequestMapping("/settings")
    public String settigns(ModelMap model) {
        model.put("cabinet", "user");
        model.put("page", "settings");
        return VIEW_USER_SETTINGS;
    }

    @RequestMapping(value = {"/reviews", "/", "", "/reviews/*"}, method = RequestMethod.GET)
    public String reviewsPublic(ModelMap model) {
        long companyId = 1;
        Company company = companyService.getCompany(companyId);
        Report<Point> report = pointService.getPoints(companyId, Pager.ALL_RECORDS);

        model.put("points", report.getItems());
        model.put("count", report.getCount());
        model.put("items_per_page", ITEMS_PER_PAGE_REVIEW);
        model.put("page", "my_feed");
        model.put("cabinet", "user");
        return VIEW_REVIEWS_LIST;
    }

    @RequestMapping(value = "/reviews/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap reviewsPublicData(
            @RequestParam("point") Long point,
            @RequestParam("feed_type") String feedType,
            @RequestParam("date_to") Date toDate,
            @RequestParam("date_from") Date fromDate,
            @RequestParam("sort_by") String sortBy,
            @RequestParam(value="page_number", defaultValue="0") Integer page) {

        ModelMap response = new ModelMap();
        try {
            Integer companyId = 1;
            ReviewQueryData query = queryFilterBuilder(point, feedType, toDate, fromDate, sortBy, page);
            Report<Review> report = reviewService.getPublicReviews(companyId, query);

            List<Map> data = new ArrayList<Map>();
            for (Review review : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message", review.getMessage());
                map.put("name", review.getAuthor().getName());
                map.put("date", review.getCreatedDT());
                map.put("point", review.getPoint().getAddress().getAddressLine1());
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
       Pager pager = new Pager(page * ITEMS_PER_PAGE_REVIEW, ITEMS_PER_PAGE_REVIEW);
       return new ReviewQueryData(pointIds, fromDate, toDate, contentType, null, null, pager, sort);
    }
}
