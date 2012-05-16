package com.redwerk.likelabs.web.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/static")
public class StaticController {
    
    
    @RequestMapping(value = {"/contact", "/contact/"}, method = RequestMethod.GET)
    public String contactUs() {
        return "contact_us";
    }

    @RequestMapping(value = {"/about", "/about/"}, method = RequestMethod.GET)
    public String aboutUs() {
        return "about";
    }

}
