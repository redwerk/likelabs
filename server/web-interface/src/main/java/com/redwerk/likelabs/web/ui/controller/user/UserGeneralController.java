package com.redwerk.likelabs.web.ui.controller.user;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PhotoService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.user.UserSettingsData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.photo.Photo;
import com.redwerk.likelabs.domain.model.photo.PhotoStatus;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.review.Review;
import com.redwerk.likelabs.domain.model.review.ReviewStatus;
import com.redwerk.likelabs.domain.model.review.exception.NotAuthorizedReviewUpdateException;
import com.redwerk.likelabs.web.ui.dto.CompanyDto;
import com.redwerk.likelabs.web.ui.dto.PointDto;
import com.redwerk.likelabs.web.ui.dto.ReviewFilterDto;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import com.redwerk.likelabs.web.ui.utils.JsonResponseBuilder;
import com.redwerk.likelabs.web.ui.utils.EnumEditor;
import com.redwerk.likelabs.web.ui.utils.QueryFilterBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * 
 * Secure on Controller uses {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 * All methods for mapping must have parameter userId
 */
@PreAuthorize("@decisionAccess.permissionUser(principal, #userId)")
@Controller
@RequestMapping(value = "/user/{userId}")
public class UserGeneralController {

    private static final String VIEW_USER_PHOTOS = "user/photos";
    private static final String VIEW_USER_SETTINGS = "user/settings";
    private static final String VIEW_REVIEWS_LIST = "user/review_list";
    private static final String VIEW_REVIEW_DETAILS = "review_details";

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

    @RequestMapping(value = "/review/{reviewId}", method = RequestMethod.GET)
    public String getReviewDetails(ModelMap model, @PathVariable Long userId, @PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        model.put("not_approved", false);
        model.put("review",review);
        return VIEW_REVIEW_DETAILS;
    }

    @RequestMapping(value="/photo", method=RequestMethod.GET)
    public String photos(ModelMap model, @PathVariable Long userId) {
        
        model.put("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_PHOTO);
        model.put("page", "my_photos");
        return VIEW_USER_PHOTOS;
    }

    @RequestMapping(value="/photo/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap photoData(@PathVariable Long userId,
                                 @RequestParam("page") Integer page, @RequestParam("status") PhotoStatus status) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {    
            Report<Photo> photos = photoService.getPhotos(userId, status, QueryFilterBuilder.buildPagerPhoto(page));
            List<Map> data = new ArrayList<Map>();
            for (Photo photo : photos.getItems()) {
                if (!photo.getStatus().equals(PhotoStatus.SELECTED)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", photo.getId());
                    map.put("status", photo.getStatus().toString());
                    data.add(map);
                }
            }
            resBuilder.setData(data);
            resBuilder.addCustomFieldData("count", photos.getCount());
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value="/photo/{photoId}/status", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changePhotoStatus(@PathVariable Long userId, @PathVariable Long photoId, @RequestParam PhotoStatus status) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            photoService.updatePhoto(photoId, status);
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String initSettings(ModelMap model, @PathVariable Long userId) {

        model.put("user", new UserDto(userService.getUser(userId)));
        model.put("page", "settings");
        return VIEW_USER_SETTINGS;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String submitSettings(ModelMap model, @PathVariable Long userId,
                     @ModelAttribute UserDto user, BindingResult result, SessionStatus status) {
        try {
            userService.updateSettings(userId, new UserSettingsData(user.getPublishInSN(), user.getEnabledEvents()));
            status.setComplete();
            model.clear();
        } catch (Exception e) {
            log.error(e,e);
            model.put("page", "settings");
            model.put("user", user);
            model.put("success", false);
            model.put("error", e.getMessage());
            return VIEW_USER_SETTINGS;
        }
        return "redirect:/user/" + userId;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    public String feed(ModelMap model, @PathVariable Long userId) {

        List<Company> companies = companyService.getCompaniesForClient(userId);
        List<CompanyDto> companiesDto = new ArrayList<CompanyDto>();
        if (companies != null) {
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
        }
        model.put("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_REVIEW);
        model.put("page", "my_feed");
        return VIEW_REVIEWS_LIST;
    }

    @RequestMapping(value = "/feed/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap getFeedData(@PathVariable Long userId, @ModelAttribute ReviewFilterDto filter) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            List<Long> companyIds = new ArrayList<Long>();
            if (filter.getCompany() == null) {
                List<Company> companies = companyService.getCompaniesForClient(userId);
                for (Company company : companies) {
                    companyIds.add(company.getId());
                }
            } else {
                companyIds.add(filter.getCompany());
            }
            Report<Review> report = reviewService.getUserReviews(userId, companyIds, QueryFilterBuilder.buildReviewQuery(filter));
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
            resBuilder.setData(data);
            resBuilder.addCustomFieldData("count", report.getCount());
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/feed/{reviewId}/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap editReview(@PathVariable Long userId, @PathVariable Long reviewId,
                  @RequestParam("message") String message) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            reviewService.updateReview(userId, reviewId, message);
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }
    
    @RequestMapping(value = "/feed/{reviewId}/remove", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap removeReview(@PathVariable Long userId, @PathVariable Long reviewId) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            reviewService.deleteReview(reviewId);
        } catch (NotAuthorizedReviewUpdateException e) {
            log.error(e,e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("review.status.authority.invalid"));
        } catch (Exception e) {
            log.error(e,e);
            resBuilder.setNotSuccess( messageTemplateService.getMessage("review.status.change.failed"));
        }
        return resBuilder.getModelResponse();
    }

    @PreAuthorize("permitAll")
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(PhotoStatus.class, new EnumEditor(PhotoStatus.class));
    }
}
