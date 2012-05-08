package com.redwerk.likelabs.web.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final String VIEW_ADMIN_PANEL = "admin/panel";

    @RequestMapping(value = "/panel", method = RequestMethod.GET)
    public String adminPanelGet(ModelMap model) {

        return VIEW_ADMIN_PANEL;
    }

    @RequestMapping(value = "/panel", method = RequestMethod.POST)
    public String adminPanelPost(ModelMap model, @RequestParam(value = "tos", required = true) String tos,
            @RequestParam(value = "bodyemail", required = true) String bodyemail,
            @RequestParam(value = "bodysms", required = true) String bodysms)  {
/*
        try {
                PropertiesConfiguration conf = new PropertiesConfiguration("/messages/messages.properties");
                conf.setProperty("message.tos", tos);
                conf.setProperty("message.email.registration.body", bodyemail);
                conf.setProperty("message.sms.registration", bodysms);
                conf.save();
        } catch (ConfigurationException ex) {
            model.addAttribute("error", "Don't saved. Please try again.");
            return VIEW_ADMIN_PANEL;
        }
*/
        return "redirect:/index";
    }
}
