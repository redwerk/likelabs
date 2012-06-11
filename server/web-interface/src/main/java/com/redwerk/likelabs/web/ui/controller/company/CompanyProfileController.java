package com.redwerk.likelabs.web.ui.controller.company;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.application.dto.company.CompanyPageData;
import com.redwerk.likelabs.application.dto.user.UserData;
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
import com.redwerk.likelabs.web.ui.validator.EmailValidator;
import com.redwerk.likelabs.web.ui.validator.PhoneValidator;
import com.redwerk.likelabs.web.ui.validator.SocialLinkValidator;
import com.redwerk.likelabs.web.ui.validator.UserProfileValidator;
import com.redwerk.likelabs.web.ui.validator.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

/*
 * security use {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 */
@PreAuthorize("@decisionAccess.permissionCompany(principal, #companyId)")
@Controller
@RequestMapping(value = "/company/{companyId}/profile")
public class CompanyProfileController {

    private final static String VIEW_COMPANY_PROFILE = "company/company_profile";
    private final static Byte MAX_LENGTH_PHONE = 20;
    private final static Byte MAX_LENGTH_EMAIL = 40;
    private final static Byte MAX_LENGTH_SOCIAL_URL = 100;

    private final static byte NEW_USER_ID = 0;

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
        model.put("cabinet", "company");
        return VIEW_COMPANY_PROFILE;

    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpSession session, @PathVariable Long companyId,
               @ModelAttribute("company") CompanyDto companyDto,
                            BindingResult result, SessionStatus status) {

        companyValidator.validate(companyDto, result);
        if (result.hasErrors()) {
            model.addAttribute("page", "profile");
            model.put("cabinet", "company");
            return VIEW_COMPANY_PROFILE;
        }
        try {
            if (session.getAttribute("logo") != null && (Long)((Map<String,Object>)session.getAttribute("logo")).get("companyId") == companyId) {
                byte[] image = (byte[])((Map<String,Object>)session.getAttribute("logo")).get("image");
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
            model.put("cabinet", "company");
            session.removeAttribute("logo");
            return VIEW_COMPANY_PROFILE;
        } catch (Exception e) {
            log.error(e, e);
            model.addAttribute("error",true);
            model.addAttribute("message","Server error. Try again later.");
            model.addAttribute("page", "profile");
            model.put("cabinet", "company");
            return VIEW_COMPANY_PROFILE;
        }
        model.clear();
        session.removeAttribute("logo");
        status.setComplete();
        return "redirect:/company/" + companyId;
    }

    @RequestMapping(value = "/cancel",method = RequestMethod.GET)
    public String processCancel(ModelMap model, HttpSession session, @PathVariable Long companyId) {
        model.clear();
        session.removeAttribute("logo");
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

    @RequestMapping(value = "/point/{pointId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deletePoint(HttpSession session, @PathVariable Integer companyId, @PathVariable("pointId") Long pointId) {

        ModelMap response = new ModelMap();
        try {
            pointService.deletePoint(pointId);
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Point  not deleted");
        }
        return response;
    }

    @RequestMapping(value = "/admin/{adminId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @PathVariable("adminId") Long adminId) {

        ModelMap response = new ModelMap();
        try {
            companyService.removeAdmin(companyId, adminId);
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Company Admin not deleted");
        }
        return response;
    }

    @RequestMapping(value = "/page/{pageId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteSocialPage(HttpSession session, @PathVariable Integer companyId, @PathVariable("pageId") String pageId) {

        ModelMap response = new ModelMap();
        try {
            companyService.detachPage(companyId, pageId);
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Social page not deleted.");
        }
        return response;
    }

    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap editCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @ModelAttribute("user") UserDto user) {
        ModelMap response = new ModelMap();

        List<String> errors = userValidator.validate(user, messageTemplateService);
        if (!errors.isEmpty()) {
            response.put("errors",errors);
            response.put("success", false);
            return response;
        }
        try {
            User u = userService.getUser(user.getId());
            userService.updateUser(user.getId(), new UserData(user.getPhone(), user.getPassword(), user.getEmail(), u.isPublishInSN(), u.getEnabledEvents()));
            response.put("success", true);
        } catch (DuplicatedUserException e) {
            log.error(e, e);
            response.put("success", false);
            errors.add(messageTemplateService.getMessage("message.registration.user.duplicated"));
        } catch (EmailMessagingException e) {
            log.error(e, e);
            response.put("success", false);
            errors.add(messageTemplateService.getMessage("message.registration.failed.send.email"));
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            errors.add("Company administrator not added  Server error.");
        }
        response.put("errors",errors);
        return response;
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @ModelAttribute("user") UserDto user) {
        ModelMap response = new ModelMap();

        List<String> errors = validateUser(user);
        if (!errors.isEmpty()) {
            response.put("errors",errors);
            response.put("success", false);
            return response;
        }
        try {
            companyService.createAdmin(companyId, new CompanyAdminData(user.getPhone(), user.getEmail()));
            response.put("success", true);
        } catch (DuplicatedUserException e) {
            log.error(e, e);
            response.put("success", false);
            errors.add(messageTemplateService.getMessage("message.registration.user.duplicated"));
        } catch (EmailMessagingException e) {
            log.error(e, e);
            response.put("success", false);
            errors.add(messageTemplateService.getMessage("message.registration.failed.send.email"));
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            errors.add("Company administrator not added  Server error.");
        }
        response.put("errors",errors);
        return response;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addSocialPage(HttpSession session, @PathVariable Integer companyId, @RequestParam("type") String type,
                                                              @RequestParam("url") String url) {

        ModelMap response = new ModelMap();
        if (!new SocialLinkValidator().isValid(url)) {
            response.put("error", messageTemplateService.getMessage("company.profile.invalid.url"));
            response.put("success", false);
            return response;
        }
        if (url.length() > MAX_LENGTH_SOCIAL_URL) {
            response.put("success", false);
            response.put("error", messageTemplateService.getMessage("company.profile.invalid.url.length", MAX_LENGTH_SOCIAL_URL.toString()));
            return response;
        }
        try {
            SocialNetworkType socialNetworkType = SocialNetworkType.valueOf(type.toUpperCase(Locale.ENGLISH));
            companyService.attachPage(companyId, new CompanyPageData(socialNetworkType, url));
            response.put("success", true);
        } catch (SNGeneralException e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Social page not added. Page not found");
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Social page not added. Server error.");
        }
        return response;
    }

    @RequestMapping(value = "/logo", method = RequestMethod.POST)
    public String uploadLogo(HttpSession session, @PathVariable Long companyId, @RequestParam("logo") MultipartFile image) {
        try {
            Map<String,Object> logo = new HashMap<String, Object>();
            logo.put("companyId", companyId);
            logo.put("image", image.getBytes());
            session.setAttribute("logo", logo);
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
            if (session.getAttribute("logo") != null && (Long)((Map<String,Object>)session.getAttribute("logo")).get("companyId") == companyId) {
                Map<String,Object> logo = (Map<String,Object>)session.getAttribute("logo");
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

    private List<String> validateUser(UserDto user) {

        final Byte MAX_LENGTH_PHONE = 20;
        final Byte MAX_LENGTH_EMAIL = 40;
        final Byte MAX_LENGTH_PASSWORD = 40;

        List<String> errors = new ArrayList<String>();
        if (!new EmailValidator().isValid(user.getEmail())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.email"));
        }
        if (!new PhoneValidator().isValid(user.getPhone())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.phone"));
        }
        if (user.getEmail().length() > MAX_LENGTH_EMAIL) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.email", MAX_LENGTH_EMAIL.toString()));
        }
        if (user.getPhone().length() > MAX_LENGTH_PHONE) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.phone", MAX_LENGTH_PHONE.toString()));
        }
        if (user.getId() == NEW_USER_ID && StringUtils.isBlank(user.getPassword())) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.password"));
        }
        if (user.getPassword().length() > MAX_LENGTH_PASSWORD) {
            errors.add(messageTemplateService.getMessage("user.profile.invalid.length.password", MAX_LENGTH_PASSWORD.toString()));
        }
        return errors;
    }

    private class CompanyProfileValidator implements org.springframework.validation.Validator {

        private static final int MAX_LENGTH_NAME = 80;
        private static final int MAX_LENGTH_PHONE = 20;
        private static final int MAX_LENGTH_EMAIL = 40;
        private final Validator mailValidator = new EmailValidator();
        private final Validator phoneValidator = new PhoneValidator();

        @Override
        public boolean supports(Class<?> clazz) {
            return CompanyDto.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name",
                    "company.profile.invalid.name.required", "Please fill in the required field.");

            CompanyDto company = (CompanyDto) target;

            if (!mailValidator.isValid(company.getEmail())) {
                errors.rejectValue("email", "company.profile.invalid.email", "Please enter valid field.");
            }
            if (!phoneValidator.isValid(company.getPhone())) {
                errors.rejectValue("phone", "company.profile.invalid.phone", "Please enter valid field.");
            }
            if (company.getPhone() != null && company.getPhone().length() > MAX_LENGTH_PHONE) {
                errors.rejectValue("phone", "company.profile.invalid.phone.length", new Byte[]{MAX_LENGTH_PHONE} ,"Maximum length allowed is "+MAX_LENGTH_PHONE+" symbols.");
            }
            if (company.getName() != null && company.getName().length() > MAX_LENGTH_NAME) {
                errors.rejectValue("name", "company.profile.invalid.name.length", new Byte[]{MAX_LENGTH_NAME} ,"Maximum length allowed is "+MAX_LENGTH_NAME+" symbols.");
            }
            if (company.getEmail() != null && company.getEmail().length() > MAX_LENGTH_EMAIL) {
                errors.rejectValue("email", "company.profile.invalid.email.length", new Byte[]{MAX_LENGTH_EMAIL} ,"Maximum length allowed is "+MAX_LENGTH_EMAIL+" symbols.");
            }
        }
    }
}
