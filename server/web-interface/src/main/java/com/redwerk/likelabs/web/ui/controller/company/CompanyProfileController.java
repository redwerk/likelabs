package com.redwerk.likelabs.web.ui.controller.company;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.application.dto.company.CompanyPageData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.service.sn.exception.SNGeneralException;
import com.redwerk.likelabs.domain.model.SocialNetworkType;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.company.CompanySocialPage;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.web.ui.controller.dto.CompanyDto;
import com.redwerk.likelabs.web.ui.validator.EmailValidator;
import com.redwerk.likelabs.web.ui.validator.PhoneValidator;
import com.redwerk.likelabs.web.ui.validator.SocialLinkValidator;
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

@Controller
@RequestMapping(value = "/company/{companyId}/profile")
public class CompanyProfileController {

    private final static int NEW_RECORD_ID = 0;
    private final static String VIEW_COMPANY_PROFILE = "company_profile";
    private final static Byte MAX_LENGTH_PHONE = 20;
    private final static Byte MAX_LENGTH_EMAIL = 40;
    private final static Byte MAX_LENGTH_SOCIAL_URL = 100;

    private CompanyProfileValidator validator = new CompanyProfileValidator();
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PointService pointService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model, @PathVariable Integer companyId) {

        CompanyDto company;
        if (companyId > NEW_RECORD_ID) {
            Company c = companyService.getCompany(companyId);
            company = new CompanyDto(c.getId(),c.getName(), c.getPhone(), c.getEmail(), c.isModerateReviews());
            model.addAttribute("title", "Edit Company Profile");
        } else {
            model.addAttribute("title", "New Company Profile");
            company = new CompanyDto();
        }
        model.addAttribute("company", company);
        return VIEW_COMPANY_PROFILE;

    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpSession session, @PathVariable Long companyId,
               @ModelAttribute("company") CompanyDto companyDto,
                            BindingResult result, SessionStatus status) {

        validator.validate(companyDto, result);
        if (result.hasErrors()) {
            return VIEW_COMPANY_PROFILE;
        }
        try {
            if (session.getAttribute("logo") != null && (Long)((Map<String,Object>)session.getAttribute("logo")).get("companyId") == companyId) {
                companyService.updateCompany(companyId, 
                                 new CompanyData(companyDto.getName(), companyDto.getPhone(), companyDto.getEmail(), companyDto.isModerate()),
                                 (byte[])((Map<String,Object>)session.getAttribute("logo")).get("image"));
            } else {
                companyService.updateCompany(companyId,
                                 new CompanyData(companyDto.getName(), companyDto.getPhone(), companyDto.getEmail(), companyDto.isModerate()));
            }
        } catch (Exception e) {
            log.error(e, e);
            model.addAttribute("error",true);
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

    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addCompanyAdmin(HttpSession session, @PathVariable Integer companyId, @RequestParam("phone") String phone,
                                                              @RequestParam("email") String email) {
        ModelMap response = new ModelMap();

        List<String> errors = new ArrayList<String>();
        if (!new EmailValidator().isValid(email)) {
            errors.add(messageTemplateService.getMessage("company.profile.ivalide.email"));
        }
        if (!new PhoneValidator().isValid(phone)) {
            errors.add(messageTemplateService.getMessage("company.profile.ivalide.phone"));
        }
        if (email.length() > MAX_LENGTH_EMAIL) {
            errors.add(messageTemplateService.getMessage("company.profile.ivalide.email.length", MAX_LENGTH_EMAIL.toString()));
        }
        if (phone.length() > MAX_LENGTH_PHONE) {
            errors.add(messageTemplateService.getMessage("company.profile.ivalide.phone.length", MAX_LENGTH_PHONE.toString()));
        }
        if (!errors.isEmpty()) {
            response.put("errors",errors);
            response.put("success", false);
            return response;
        }
        response.put("success", true);
        try {
            companyService.createAdmin(companyId, new CompanyAdminData(phone, email));
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            errors.add("Company administrator not added  Server error.");
        }
        return response;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addSocialPage(HttpSession session, @PathVariable Integer companyId, @RequestParam("type") String type,
                                                              @RequestParam("url") String url) {

        ModelMap response = new ModelMap();
        if (!new SocialLinkValidator().isValid(url)) {
            response.put("error", messageTemplateService.getMessage("company.profile.ivalide.url"));
            response.put("success", false);
            return response;
        }
        if (url.length() > MAX_LENGTH_SOCIAL_URL) {
            response.put("success", false);
            response.put("error", messageTemplateService.getMessage("company.profile.ivalide.url.length", MAX_LENGTH_SOCIAL_URL.toString()));
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
    public String uploadLogo(HttpSession session, @PathVariable Integer companyId, @RequestParam("logo") MultipartFile image) {
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
                    "company.profile.ivalide.name.required", "Please fill in the required field.");

            CompanyDto company = (CompanyDto) target;

            if (!mailValidator.isValid(company.getEmail())) {
                errors.rejectValue("email", "company.profile.ivalide.email", "Please enter valid field.");
            }
            if (!phoneValidator.isValid(company.getPhone())) {
                errors.rejectValue("phone", "company.profile.ivalide.phone", "Please enter valid field.");
            }
            if (company.getPhone() != null && company.getPhone().length() > MAX_LENGTH_PHONE) {
                errors.rejectValue("phone", "company.profile.ivalide.phone.length", new Byte[]{MAX_LENGTH_PHONE} ,"Maximum length allowed is "+MAX_LENGTH_PHONE+" symbols.");
            }
            if (company.getName() != null && company.getName().length() > MAX_LENGTH_NAME) {
                errors.rejectValue("name", "company.profile.ivalide.name.length", new Byte[]{MAX_LENGTH_NAME} ,"Maximum length allowed is "+MAX_LENGTH_NAME+" symbols.");
            }
            if (company.getEmail() != null && company.getEmail().length() > MAX_LENGTH_EMAIL) {
                errors.rejectValue("email", "company.profile.ivalide.email.length", new Byte[]{MAX_LENGTH_EMAIL} ,"Maximum length allowed is "+MAX_LENGTH_EMAIL+" symbols.");
            }
        }
    }
}
