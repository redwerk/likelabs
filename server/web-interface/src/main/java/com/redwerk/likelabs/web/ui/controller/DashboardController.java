package com.redwerk.likelabs.web.ui.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/dashboard")
public class DashboardController {

    private  static final String VIEW_DASHBOARD = "dashboard";


    @RequestMapping(value = {"/",""}, method = RequestMethod.GET)
    public String dasboard(ModelMap model) {

        return VIEW_DASHBOARD;
    }
}
