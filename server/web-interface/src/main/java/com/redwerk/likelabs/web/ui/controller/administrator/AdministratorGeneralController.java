package com.redwerk.likelabs.web.ui.controller.administrator;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.NotificationService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyAdminData;
import com.redwerk.likelabs.application.dto.company.CompanyData;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.application.dto.user.UserProfileData;
import com.redwerk.likelabs.application.impl.registration.exception.DuplicatedUserException;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.event.EventType;
import com.redwerk.likelabs.domain.model.notification.NotificationInterval;
import com.redwerk.likelabs.domain.model.notification.Period;
import com.redwerk.likelabs.domain.model.notification.WarningType;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.domain.model.user.UserStatus;
import com.redwerk.likelabs.web.ui.dto.NotificationSettingsDto;
import com.redwerk.likelabs.web.ui.dto.CompanyDto;
import com.redwerk.likelabs.web.ui.dto.NotificationIntervalDto;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import com.redwerk.likelabs.web.ui.security.Authenticator;
import com.redwerk.likelabs.web.ui.utils.JsonResponseBuilder;
import com.redwerk.likelabs.web.ui.utils.EnumEditor;
import com.redwerk.likelabs.web.ui.utils.QueryFilterBuilder;
import com.redwerk.likelabs.web.ui.validator.CompanyProfileValidator;
import com.redwerk.likelabs.web.ui.validator.UserProfileValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

@PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
@Controller
@RequestMapping(value = "/administrator")
public class AdministratorGeneralController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Authenticator authenticator;
    @Autowired
    private MessageTemplateService messageTemplateService;
    private static final String VIEW_ADMIN_COMPANIES = "admin/companies";
    private static final String VIEW_ADMIN_USERS = "admin/users";
    private static final String VIEW_ADMIN_SETTINGS = "admin/settings";
    private static final byte NEW_USER_ID = 0;
    private final UserProfileValidator userValidator = new UserProfileValidator();
    private final CompanyProfileValidator companyValidator = new CompanyProfileValidator();
    private final Logger log = LogManager.getLogger(getClass());

    @RequestMapping(value = {"/", ""})
    public String cabinet(ModelMap model) {

        return "redirect:/administrator/companies";
    }

    @RequestMapping(value = "/companies")
    public String listCompanies(ModelMap model) {

        model.put("count", companyService.getCompaniesCount());
        model.put("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_COMPANY);
        model.put("page", "companies");
        return VIEW_ADMIN_COMPANIES;
    }

    @RequestMapping(value = "/companies/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap listCompanyData(@RequestParam(value = "page", defaultValue = "0") Integer page) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            Report<CompanyReportItem> report = companyService.getCompanies(QueryFilterBuilder.buildPagerCompanies(page));
            List<Map> data = new ArrayList<Map>();
            for (CompanyReportItem reportItem : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                Company company = reportItem.getCompany();
                map.put("id", company.getId());
                map.put("name", company.getName());
                map.put("email", company.getEmail());
                map.put("phone", company.getPhone());
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

    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteCompany(@PathVariable Long companyId) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            companyService.deleteCompany(companyId);
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/companies/add", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addCompany(@ModelAttribute("company") CompanyDto company,
            @RequestParam("admin_phone") String adminPhone,
            @RequestParam("admin_email") String adminEmail) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        List<String> errors = companyValidator.validateOnCreate(company, new CompanyAdminData(adminPhone, adminEmail), messageTemplateService);
        if (!errors.isEmpty()) {
            resBuilder.addMessages(errors);
            resBuilder.addCustomFieldData("valid", false);
            return resBuilder.getModelResponse();
        }
        resBuilder.addCustomFieldData("valid", true);
        try {
            companyService.createCompany(authenticator.getCurrentUserId(),
                    new CompanyData(company.getName(), company.getPhone(), company.getEmail()),
                    new CompanyAdminData(adminPhone, adminEmail));
        } catch (DuplicatedUserException e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("message.registration.user.duplicated"));
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess("Cannot added company");
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listUsers(ModelMap model) {

        Report<User> report = userService.getRegularUsers(Pager.ALL_RECORDS);
        model.put("count", report.getCount());
        model.put("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_USER);
        model.put("page", "users");
        return VIEW_ADMIN_USERS;
    }

    @RequestMapping(value = "/users/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap listUsersData(@RequestParam(value = "page", defaultValue = "0") Integer page) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            Report<User> report = userService.getRegularUsers(QueryFilterBuilder.buildPagerUsers(page));
            List<Map> data = new ArrayList<Map>();
            for (User user : report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", user.getId());
                map.put("name", user.getName());
                map.put("email", user.getEmail());
                map.put("phone", user.getPhone());
                map.put("password", user.getPassword());
                map.put("statusValue", user.getStatus());
                map.put("status", messageTemplateService.getMessage("user.status." + user.getStatus()));
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

    @RequestMapping(value = "/users/status", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeStatusUser(@ModelAttribute("user") UserDto user) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            userService.updateStatus(user.getId(), authenticator.getCurrentUserId(), user.getStatus());
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addEditUser(@ModelAttribute("user") UserDto user) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        List<String> errors = userValidator.validate(user, messageTemplateService);
        if (!errors.isEmpty()) {
            resBuilder.addMessages(errors);
            resBuilder.addCustomFieldData("valid", false);
            return resBuilder.getModelResponse();
        }
        resBuilder.addCustomFieldData("valid", true);
        try {
            if (user.getId() > NEW_USER_ID) {
                userService.updateProfile(user.getId(), new UserProfileData(user.getPhone(), user.getPassword(), user.getEmail()));
            } else {
                userService.createUser(authenticator.getCurrentUserId(), new UserProfileData(user.getPhone(), user.getPassword(), user.getEmail()));
            }
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(e.getMessage());
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String initSettings(ModelMap model) {

        List<NotificationInterval> intervals = notificationService.getIntervals();
        NotificationSettingsDto settings = new NotificationSettingsDto(intervals);
        model.put("settings", settings);
        model.put("itemsAll", settings.getAllItemsForOptions(messageTemplateService));
        model.put("itemsEmailAbsent", settings.getItemsForOptions(messageTemplateService,Period.NEVER, Period.DAILY, Period.MONTHLY, Period.WEEKLY));
        model.put("page", "settings");
        return VIEW_ADMIN_SETTINGS;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String submitSettings(ModelMap model,
            @ModelAttribute("settings") NotificationSettingsDto settings, BindingResult result, SessionStatus status) {

        notificationService.updateIntervals(settings.getIntervals());
        status.setComplete();
        model.clear();
        return "redirect:/administrator";
    }

    @PreAuthorize("permitAll")
    @InitBinder
    public void initBinder(WebDataBinder binder, HttpServletRequest request) {

        binder.registerCustomEditor(Period.class, new EnumEditor(Period.class));
        binder.registerCustomEditor(EventType.class, new EnumEditor(EventType.class));
        binder.registerCustomEditor(WarningType.class, new EnumEditor(WarningType.class));
        binder.registerCustomEditor(UserStatus.class, new EnumEditor(UserStatus.class));
    }
}
