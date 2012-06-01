package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.ReviewService;
import com.redwerk.likelabs.application.UserService;
import com.redwerk.likelabs.application.dto.Report;
import com.redwerk.likelabs.application.dto.company.CompanyReportItem;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.company.Company;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.user.User;
import com.redwerk.likelabs.web.ui.controller.dto.ProfileData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

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

    public static final int ITEMS_PER_PAGE_COMPANY = 10;
    
    private final Logger log = LogManager.getLogger(getClass());
    
    @RequestMapping(value={"/", "", "/companies"})
    public String companies(ModelMap model) {
        model.put("count", companyService.getCompaniesCount());
        model.put("items_per_page", ITEMS_PER_PAGE_COMPANY);
        model.put("cabinet", "admin");
        model.put("page", "companies");
        return "admin/companies";
    }

    @RequestMapping(value = "/companies/data", method = RequestMethod.GET)
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
                map.put("email", company.getEmail());
                map.put("phone", company.getPhone());
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

    @RequestMapping(value="/users")
    public String users(ModelMap model) {
        model.put("count", companyService.getCompaniesCount());
        model.put("items_per_page", ITEMS_PER_PAGE_COMPANY);
        model.put("cabinet", "admin");
        model.put("page", "users");
        return "admin/users";
    }

    @RequestMapping(value = "/users/data", method = RequestMethod.GET)
    @ResponseBody
    public ModelMap listUsersData(@RequestParam(value="page_number", defaultValue="0") Integer page) {

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
                map.put("email", company.getEmail());
                map.put("phone", company.getPhone());
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

    @RequestMapping(value="/settings")
    public String settings(ModelMap model) {
        model.put("cabinet", "admin");
        model.put("page", "settings");
        return "admin/settings";
    }


    @RequestMapping(value="/profile")
    public String profile(ModelMap model) {
        User user = userService.getUser(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.put("profile", new ProfileData(user.getPhone(), "", user.getEmail()));
        model.put("cabinet", "admin");
        model.put("page", "profile");
        return "admin/profile";
    }

}
