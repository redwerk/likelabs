package com.redwerk.likelabs.web.ui.controller.company;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.application.dto.company.CompanyPageData;
import com.redwerk.likelabs.application.dto.user.UserProfileData;
import com.redwerk.likelabs.application.messaging.exception.EmailMessagingException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.service.sn.exception.SNGeneralException;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.company.exception.CompanyLogoTooBigException;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.exception.DuplicatedUserException;
import com.redwerk.likelabs.web.ui.dto.CompanyDto;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import com.redwerk.likelabs.web.ui.utils.JsonResponseBuilder;
import com.redwerk.likelabs.web.ui.validator.CompanyProfileValidator;
import com.redwerk.likelabs.web.ui.validator.SocialLinkValidator;
import com.redwerk.likelabs.web.ui.validator.UserProfileValidator;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * Secure on Controller uses {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 * All methods for mapping must have parameter companyId
 */
@PreAuthorize("@decisionAccess.permissionCompany(principal, #companyId)")
@Controller
@RequestMapping(value = "/company/{companyId}/profile")
public class CompanyProfileController {

    private final static String VIEW_COMPANY_PROFILE = "company/company_profile";
    private final static Byte MAX_LENGTH_SOCIAL_URL = 100;
    private final static String SESSION_ATTRIBUTE_LOGO = "logo";

    private CompanyProfileValidator companyValidator = new CompanyProfileValidator();
    private UserProfileValidator userValidator = new UserProfileValidator();
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PointService pointService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model, @PathVariable Integer companyId) {

        Company c = companyService.getCompany(companyId);
        CompanyDto company = new CompanyDto(c.getId(),c.getName(), c.getPhone(), c.getEmail(), c.isModerateReviews());
        model.addAttribute("title", "Edit Company Profile");
        model.addAttribute("company", company);
        model.addAttribute("page", "profile");
        return VIEW_COMPANY_PROFILE;

    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpSession session, @PathVariable Long companyId,
               @ModelAttribute("company") CompanyDto companyDto,
                            BindingResult result, SessionStatus status) {

        companyValidator.validate(companyDto, result);
        if (result.hasErrors()) {
            model.addAttribute("page", "profile");
            return VIEW_COMPANY_PROFILE;
        }
        try {
            if (session.getAttribute(SESSION_ATTRIBUTE_LOGO) != null && (Long)((Map<String,Object>)session.getAttribute(SESSION_ATTRIBUTE_LOGO)).get("companyId") == companyId) {
                byte[] image = (byte[])((Map<String,Object>)session.getAttribute(SESSION_ATTRIBUTE_LOGO)).get("image");
                companyService.updateCompany(companyId,
                                 new CompanyData(companyDto.getName(), companyDto.getPhone(), companyDto.getEmail(), companyDto.isModerate()), image);
            } else {
                companyService.updateCompany(companyId,
                                 new CompanyData(companyDto.getName(), companyDto.getPhone(), companyDto.getEmail(), companyDto.isModerate()));
            }
        } catch (CompanyLogoTooBigException e) {
            log.error(e, e);
            model.addAttribute("error",true);
            model.addAttribute("message","The image size should not exceed 1Mb.");
            model.addAttribute("page", "profile");
            session.removeAttribute("logo");
            return VIEW_COMPANY_PROFILE;
        } catch (Exception e) {
            log.error(e, e);
            model.addAttribute("error",true);
            model.addAttribute("message","Server error. Try again later.");
            model.addAttribute("page", "profile");
            return VIEW_COMPANY_PROFILE;
        }
        model.clear();
        session.removeAttribute(SESSION_ATTRIBUTE_LOGO);
        status.setComplete();
        return "redirect:/company/" + companyId;
    }

    @RequestMapping(value = "/cancel",method = RequestMethod.GET)
    public String processCancel(ModelMap model, HttpSession session, @PathVariable Long companyId) {
        model.clear();
        session.removeAttribute(SESSION_ATTRIBUTE_LOGO);
        return "redirect:/company/" + companyId;
    }

    @ModelAttribute("admins")
    public Set<User> adminsList(@PathVariable Integer companyId) {

        Set<User> admins = companyService.getCompany(companyId).getAdmins();
        return admins;
    }

    @ModelAttribute("socialPages")
    public Set<CompanySocialPage> socialPagesList(@PathVariable Integer companyId) {

        Set<CompanySocialPage> socialPages = companyService.getCompany(companyId).getSocialPages();
        return socialPages;
    }

    @ModelAttribute("points")
    public List<Point> pointsList(@PathVariable Integer companyId) {

        List<Point> points = pointService.getPoints(companyId, Pager.ALL_RECORDS).getItems();
        return points;
    }

    @RequestMapping(value = "/point/add", method = RequestMethod.GET)
    public String addPoint(HttpSession session, @PathVariable Integer companyId) {

        session.removeAttribute(PointProfileController.SESSION_ATR_TABLETS);
        return "redirect:/company/" + companyId + "/point/0/profile";
    }

    @RequestMapping(value = "/point/{pointId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deletePoint(HttpSession session, @PathVariable Integer companyId, @PathVariable("pointId") Long pointId) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            pointService.deletePoint(pointId);
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("company.point.not.deleted"));
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/admin/{adminId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @PathVariable("adminId") Long adminId) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            companyService.removeAdmin(companyId, adminId);
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("company.admin.not.deleted"));
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/page/{pageId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteSocialPage(HttpSession session, @PathVariable Integer companyId, @PathVariable("pageId") String pageId) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            companyService.detachPage(companyId, pageId);
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("company.page.not.deleted"));
        }
        return resBuilder.getModelResponse();
    }

    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap editCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @ModelAttribute("user") UserDto user) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        List<String> errors = userValidator.validate(user, messageTemplateService);
        if (!errors.isEmpty()) {
            resBuilder.addMessages(errors);
            resBuilder.addCustomFieldData("valid", false);
            return resBuilder.getModelResponse();
        }
        resBuilder.addCustomFieldData("valid", true);
        try {
            userService.updateProfile(user.getId(), new UserProfileData(user.getPhone(), user.getPassword(), user.getEmail()));
        } catch (DuplicatedUserException e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("message.registration.user.duplicated"));
        } catch (EmailMessagingException e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("message.registration.failed.send.email"));
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess("Company administrator not added  Server error.");
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @ModelAttribute("user") UserDto user) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        List<String> errors = userValidator.validateWithoutPassword(user, messageTemplateService);
        if (!errors.isEmpty()) {
            resBuilder.addMessages(errors);
            resBuilder.addCustomFieldData("valid", false);
            return resBuilder.getModelResponse();
        }
        resBuilder.addCustomFieldData("valid", true);
        try {
            companyService.createAdmin(companyId, new CompanyAdminData(user.getPhone(), user.getEmail()));
        } catch (DuplicatedUserException e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("message.registration.user.duplicated"));
        } catch (EmailMessagingException e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("message.registration.failed.send.email"));
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess("Company administrator not added  Server error.");
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addSocialPage(HttpSession session, @PathVariable Integer companyId, @RequestParam("type") String type,
                                                              @RequestParam("url") String url) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        if (!new SocialLinkValidator().isValid(url)) {
            resBuilder.addMessage(messageTemplateService.getMessage("company.profile.invalid.url"));
            resBuilder.addCustomFieldData("valid", false);
            return resBuilder.getModelResponse();
        }
        if (url.length() > MAX_LENGTH_SOCIAL_URL) {
            resBuilder.addMessage(messageTemplateService.getMessage("company.profile.invalid.url.length", MAX_LENGTH_SOCIAL_URL.toString()));
            resBuilder.addCustomFieldData("valid", false);
            return resBuilder.getModelResponse();
        }
        resBuilder.addCustomFieldData("valid", true);
        try {
            SocialNetworkType socialNetworkType = SocialNetworkType.valueOf(type.toUpperCase(Locale.ENGLISH));
            companyService.attachPage(companyId, new CompanyPageData(socialNetworkType, url));
        } catch (SNGeneralException e) {
            log.error(e, e);
            resBuilder.setNotSuccess("Social page not added. Page not found");
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess( "Social page not added. Server error.");
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/logo", method = RequestMethod.POST)
    public String uploadLogo(HttpSession session, @PathVariable Long companyId, @RequestParam("logo") MultipartFile image) {
        try {
            Map<String,Object> logo = new HashMap<String, Object>();
            logo.put("companyId", companyId);
            logo.put("image", image.getBytes());
            session.setAttribute(SESSION_ATTRIBUTE_LOGO, logo);
        } catch (IOException e) {
            log.error(e,e);
        }
        return "redirect:/company/" + companyId + "/profile";
    }

    @RequestMapping(value = "/logo", method = RequestMethod.GET)
    public void getPreviewlogo(HttpSession session, @PathVariable Long companyId,
            HttpServletResponse response) {

        ServletOutputStream out = null;
        try {
            byte[] image;
            if (session.getAttribute(SESSION_ATTRIBUTE_LOGO) != null && (Long)((Map<String,Object>)session.getAttribute(SESSION_ATTRIBUTE_LOGO)).get("companyId") == companyId) {
                Map<String,Object> logo = (Map<String,Object>)session.getAttribute(SESSION_ATTRIBUTE_LOGO);
                if (logo.get("companyId") != companyId) return;
                image = (byte[])logo.get("image");
            } else {
                Company company = companyService.getCompany(companyId);
                if (company == null || company.getLogo() == null) return;
                image = company.getLogo();
            }
            response.setContentType("image");
            out = response.getOutputStream();
            out.write(image);
            out.close();
        } catch (IOException e) {
            log.error(e, e);
        }
    }
}
