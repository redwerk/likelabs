package com.redwerk.likelabs.web.ui.controller.administrator;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.web.ui.dto.AdminNotificationSettingsDto;
import com.redwerk.likelabs.web.ui.dto.CompanyDto;
import com.redwerk.likelabs.web.ui.dto.UserDto;
import com.redwerk.likelabs.web.ui.utils.QueryFilterBuilder;
import com.redwerk.likelabs.web.ui.validator.UserProfileValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

@PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
@Controller
@RequestMapping(value = "/administrator")
public class AdministratorGeneralController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    private static final String VIEW_ADMIN_COMPANIES = "admin/companies";
    private static final String VIEW_ADMIN_USERS = "admin/users";
    private static final String VIEW_ADMIN_SETTINGS = "admin/settings";
    
    private static final byte NEW_USER_ID = 0;

    private final UserProfileValidator validator = new UserProfileValidator();

    private final Logger log = LogManager.getLogger(getClass());
    
    @RequestMapping(value={"/", ""})
    public String cabinet(ModelMap model) {

        return "redirect:/administrator/companies";
    }

    @RequestMapping(value="/companies")
    public String listCompanies(ModelMap model) {

        model.put("count", companyService.getCompaniesCount());
        model.put("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_COMPANY);
        model.put("page", "companies");
        return VIEW_ADMIN_COMPANIES;
    }

    @RequestMapping(value = "/companies/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap listCompanyData(@RequestParam(value="page", defaultValue="0") Integer page) {

        ModelMap response = new ModelMap();
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
            response.put("data", data);
            response.put("count", report.getCount());
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("message", e.getMessage());
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteCompany(@PathVariable Long companyId) {

        ModelMap response = new ModelMap();
        try {
            companyService.deleteCompany(companyId);
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("message", e.getMessage());
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping(value = "/companies/add", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addCompany(@ModelAttribute("company") CompanyDto company, @RequestParam("admin_phone") String adminPhone,
                                    @RequestParam("admin_email") String adminEmail) {

        ModelMap response = new ModelMap();
        try {
            //TODO create use company service
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("message", e.getMessage());
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping(value="/users", method = RequestMethod.GET)
    public String listUsers(ModelMap model) {

        Report<User> report = userService.getRegularUsers(Pager.ALL_RECORDS);
        model.put("count", report.getCount());
        model.put("items_per_page", QueryFilterBuilder.ITEMS_PER_PAGE_USER);
        model.put("page", "users");
        return VIEW_ADMIN_USERS;
    }

    @RequestMapping(value = "/users/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap listUsersData(@RequestParam(value="page", defaultValue="0") Integer page) {

        ModelMap response = new ModelMap();
        try {
            Report<User> report = userService.getRegularUsers(QueryFilterBuilder.buildPagerUsers(page));
            List<Map> data = new ArrayList<Map>();
            for (User user: report.getItems()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", user.getId());
                map.put("name", user.getName());
                map.put("email", user.getEmail());
                map.put("phone", user.getPhone());
                map.put("password", user.getPassword());
                if (user.isActive()) {
                    map.put("status", "Active");
                } else {
                    map.put("status", "Not activation");
                }
                data.add(map);
            }
            response.put("data", data);
            response.put("count", report.getCount());
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("message", e.getMessage());
            response.put("success", false);
        } 
        response.put("success", true);
        return response;
    }

    @RequestMapping(value = "/users/status", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap changeStatusUser(@ModelAttribute("user") UserDto user) {

        ModelMap response = new ModelMap();
        try {
            if ("deleted".equals(user.getStatus())) {
                userService.deleteUser(user.getId());
            }
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("message", e.getMessage());
            response.put("success", false);
        }
        return response;
    }
    //

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addEditUser(@ModelAttribute("user") UserDto user) {

        ModelMap response = new ModelMap();
        List<String> errors = validator.validate(user, messageTemplateService);
        if (!errors.isEmpty()) {
            response.put("errors",errors);
            response.put("success", false);
            return response;
        } 
        try {
            if (user.getId() > NEW_USER_ID) {                
                //TODO userService
            } else {
                //TODO userService
            }
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            errors.add(e.getMessage());
            response.put("errors", errors);
            response.put("success", false);
        }
        return response;
    }

    @RequestMapping(value="/settings",  method = RequestMethod.GET)
    public String initSettings(ModelMap model) {

        //TODO get from service settings
        AdminNotificationSettingsDto settings = new AdminNotificationSettingsDto(new Object());
        model.put("settings", settings);
        model.put("page", "settings");
        return VIEW_ADMIN_SETTINGS;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String submitSettings(ModelMap model,
                     @ModelAttribute("settings") AdminNotificationSettingsDto settings, BindingResult result, SessionStatus status) {
        try {
            //TODO save settings use admin service
            status.setComplete();
            model.clear();
        } catch (Exception e) {
            log.error(e,e);
            model.put("settings", settings);
            model.put("page", "settings");
            return VIEW_ADMIN_SETTINGS;
        }
        return "redirect:/administrator";
    }
}
