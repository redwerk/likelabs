package com.redwerk.likelabs.web.ui.controller.user;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PhotoService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.ReviewQueryData;
import com.redwerk.likelabs.application.dto.user.UserData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.review.ContentTypeFilter;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.SortingCriteria;
import com.redwerk.likelabs.domain.model.review.SortingOrder;
import com.redwerk.likelabs.domain.model.review.SortingRule;
import com.redwerk.likelabs.domain.model.review.exception.NotAuthorizedReviewUpdateException;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.infrastructure.security.AuthorityRole;
import com.redwerk.likelabs.web.ui.dto.CompanyDto;
import com.redwerk.likelabs.web.ui.dto.PointDto;
import com.redwerk.likelabs.web.ui.dto.ReviewFilterDto;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.expression.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

/*
 * security use {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 */
@PreAuthorize("@decisionAccess.permissionUser(principal, #userId)")
@Controller
@RequestMapping(value = "/user/{userId}")
public class UserContentController {

    private static final String VIEW_USER_PHOTOS = "user/photos";
    private static final String VIEW_USER_SETTINGS = "user/settings";
    private static final String VIEW_REVIEWS_LIST = "user/review_list";
    private static final int ITEMS_PER_PAGE = 8;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private MessageTemplateService messageTemplateService;
    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String cabinet(ModelMap model, @PathVariable Long userId) {

        return "redirect:/user/" + userId + "/feed";
    }

    @RequestMapping(value="/photo", method=RequestMethod.GET)
    public String photos(ModelMap model, @PathVariable Long userId) {
        
        model.put("items_per_page", ITEMS_PER_PAGE);
        model.put("cabinet", "user");
        model.put("page", "my_photos");
        return VIEW_USER_PHOTOS;
    }

    @RequestMapping(value="/photo/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap photoData(@PathVariable Long userId,
                                 @RequestParam("page") Integer page, @RequestParam("status") String status) {

        ModelMap response = new ModelMap();
        try {    
            PhotoStatus statusFilter = PhotoStatus.ACTIVE;
            if ("deleted".equals(status)) {
                statusFilter = PhotoStatus.DELETED;
            } 
            Pager pager = new Pager(page * ITEMS_PER_PAGE, ITEMS_PER_PAGE);
            Report<Photo> photos = photoService.getPhotos(userId, statusFilter, pager);
            List<Map> data = new ArrayList<Map>();
            for (Photo photo : photos.getItems()) {
                if (photo.getStatus().equals(PhotoStatus.ACTIVE) || photo.getStatus().equals(PhotoStatus.DELETED)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", photo.getId());
                    map.put("status", photo.getStatus().toString().toLowerCase(Locale.ENGLISH));
                    data.add(map);
                }
            }
            response.put("data", data);
            response.put("count", photos.getCount());
            response.put("success", true);
        } catch (Exception e) {
            log.error(e,e);
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @RequestMapping(value="/photo/{photoId}/status", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changePhotoStatus(@PathVariable Long userId, @PathVariable Long photoId, @RequestParam("status") String status) {

        ModelMap response = new ModelMap();
        try {
            if (status.equals("active")) {
                photoService.updatePhoto(photoId, PhotoStatus.ACTIVE);
            }
            if (status.equals("deleted")) {
                photoService.updatePhoto(photoId, PhotoStatus.DELETED);
            }
            if (status.equals("selected")) {
                photoService.updatePhoto(photoId, PhotoStatus.SELECTED);
            }
            response.put("success", true);
        } catch (Exception e) {
            log.error(e,e);
            response.put("message", e.getMessage());
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String initSettings(ModelMap model, @PathVariable Long userId) {

        User user = userService.getUser(userId);
        model.put("user", new UserDto(user));
        model.put("cabinet", "user");
        model.put("page", "settings");
        return VIEW_USER_SETTINGS;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String submitSettings(ModelMap model, @PathVariable Long userId,
                     @ModelAttribute("user") UserDto user, BindingResult result, SessionStatus status) {
        try {
            User userOldData = userService.getUser(userId);
            userService.updateUser(userId, new UserData(userOldData.getPhone(), userOldData.getPassword(),
                    userOldData.getEmail(), user.getPublishInSN(), user.getEnabledEvents()));
            status.setComplete();
            model.clear();
        } catch (Exception e) {
            log.error(e,e);
            model.put("cabinet", "user");
            model.put("page", "settings");
            model.put("user", user);
            model.put("success", false);
            model.put("message", e.getMessage());
            return VIEW_USER_SETTINGS;
        }
        return "redirect:/user/" + userId;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String feed(ModelMap model, @PathVariable Long userId) {

        List<Company> companies = companyService.getCompaniesForClient(userId);
        List<CompanyDto> companiesDto = new ArrayList<CompanyDto>();
        if (companies == null) {
            model.put("page", "my_feed");
            model.put("cabinet", "user");
            return VIEW_REVIEWS_LIST;
        }
        for (Company company : companies) {
            CompanyDto companyDto = new CompanyDto(company.getId(), company.getName());
            List<Point> points = pointService.getPointsForClient(company.getId(), userId);
            for (Point point : points) {
                companyDto.addPoint(new PointDto(point.getId(), point.getAddress().getAddressLine1()));
            }
            companiesDto.add(companyDto);
        }
        model.put("companies", companiesDto);
        model.put("count", companies.size());
        model.put("items_per_page", ITEMS_PER_PAGE);
        model.put("page", "my_feed");
        model.put("cabinet", "user");
        return VIEW_REVIEWS_LIST;
    }

    @RequestMapping(value = "/feed/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap getFeedData(@PathVariable Long userId, @ModelAttribute ReviewFilterDto filter) {

        ModelMap response = new ModelMap();
        try {
            ReviewQueryData query = queryFilterBuilder(filter);
            List<Long> companyIds = new ArrayList<Long>();
            if (filter.getCompany() == null) {
                List<Company> companies = companyService.getCompaniesForClient(userId);
                for (Company company : companies) {
                    companyIds.add(company.getId());
                }
            } else {
                companyIds.add(filter.getCompany());
            }
            Report<Review> report = reviewService.getUserReviews(getRealUserId(userId), companyIds, query);
            List<Map> data = new ArrayList<Map>();
            for (Review review : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("message", review.getMessage());
                map.put("name", review.getAuthor().getName());
                map.put("date", review.getCreatedDT());
                map.put("point", review.getPoint().getAddress().getAddressLine1());
                map.put("id", review.getId());
                map.put("containsPhoto", review.getPhoto() != null);
                map.put("status", review.getStatus().toString().toLowerCase(Locale.ENGLISH));
                map.put("active", review.isPublishedInCompanySN());
                data.add(map);
            }
            response.put("data", data);
            response.put("count", report.getCount());
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/feed/{reviewId}/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap editReview(@PathVariable Long userId, @PathVariable Long reviewId,
                  @RequestParam("message") String message) {

        ModelMap response = new ModelMap();
        try {
            reviewService.updateReview(userId, reviewId, message);
            response.put("success", true);
        } catch (Exception e) {
            log.error(e,e);
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
    
    @RequestMapping(value = "/feed/{reviewId}/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap removeReview(@PathVariable Long userId, @PathVariable Long reviewId) {

        ModelMap response = new ModelMap();
        try {
            reviewService.deleteReview(reviewId);
            response.put("success", true);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            response.put("message", messageTemplateService.getMessage("review.status.authority.invalid"));
            response.put("success", false);
        } catch (Exception e) {
            log.error(e,e);
            response.put("message", messageTemplateService.getMessage("review.status.change.failed"));
            response.put("success", false);
        }
        return response;
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
            switch (sortingCriteria) {
                case DATE:
                    sortingOrder = SortingOrder.DESCENDING;
                    break;
                case REVIEW_TYPE:
                    sortingOrder = SortingOrder.DESCENDING;
                    break;
                default:
                    sortingOrder = SortingOrder.ASCENDING;
            }
            sort = new SortingRule(sortingCriteria, sortingOrder);
        }
        Pager pager = new Pager(filter.getPage() * ITEMS_PER_PAGE, ITEMS_PER_PAGE);
        return new ReviewQueryData(pointIds, filter.getFromDate(), filter.getToDate(),
                contentType, filter.getSampleStatus(), filter.getPublishingStatus(), pager, sort);
    }

    private Long getRealUserId(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority(AuthorityRole.ROLE_COMPANY_ADMIN.toString()))) {
            return Long.parseLong(auth.getName());
        }
        return id;
    }

    @PreAuthorize("permitAll")
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
