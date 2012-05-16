package com.redwerk.likelabs.web.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/company")
public class CompanyController {
    
    private  static final String VIEW_COMPANY_LIST = "companies_list"; 


    @RequestMapping(value = {"/","", "/list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        return VIEW_COMPANY_LIST;
    }

    @RequestMapping(value = {"{companyId}/points"}, method = RequestMethod.GET)
    public String points(ModelMap model) {
        return "points_list";
    }

    @RequestMapping(value = {"{companyId}/feed"}, method = RequestMethod.GET)
    public String feed(ModelMap model) {
        return "feed";
    }
}
