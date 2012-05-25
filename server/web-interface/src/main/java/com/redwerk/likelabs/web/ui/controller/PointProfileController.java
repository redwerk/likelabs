
package com.redwerk.likelabs.web.ui.controller;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.application.dto.TabletData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.web.ui.controller.dto.PointDto;
import com.redwerk.likelabs.web.ui.controller.dto.TabletDto;
import com.redwerk.likelabs.web.ui.validator.EmailValidator;
import com.redwerk.likelabs.web.ui.validator.PhoneValidator;
import com.redwerk.likelabs.web.ui.validator.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping(value = "/company/{companyId}/point/{pointId}/profile")
public class PointProfileController {


    private final static int NEW_RECORD_ID = 0;
    private final static String SESSION_ATR_TABLETS = "tablets";
    private final static String VIEW_POINT_PROFILE = "point_profile";
    private final static Byte MAX_LENGTH_FIELD_TABLET = 20;
    private final PointProfileValidator validator = new PointProfileValidator();

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PointService pointService;

    @Autowired
    private TabletService tabletService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model, @PathVariable Integer companyId,
                        @PathVariable Integer pointId) {

        PointDto point = null;
        if (pointId > NEW_RECORD_ID) {
            Point pointDomain = pointService.getPoint(pointId);
            point = new PointDto(pointDomain);
            model.addAttribute("title", "Edit Point for ");
        } else {
            point = new PointDto();
            model.addAttribute("title", "New Point for ");
        }
        model.addAttribute("point", point);
        return VIEW_POINT_PROFILE;

    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpSession session, @PathVariable Integer companyId,
                  @PathVariable Integer pointId, @ModelAttribute("point") PointDto point,
                          BindingResult result, SessionStatus status) {

        validator.validate(point, result);
        if (result.hasErrors()) {
            return VIEW_POINT_PROFILE;
        }
        if (pointId > NEW_RECORD_ID) {
            pointService.updatePoint(pointId, point.getPointData());
        } else {
            Point newPoint = pointService.createPoint(companyId, point.getPointData());
            for (TabletDto tablet: (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS)) {
                tabletService.createTablet(newPoint.getId(), 
                        new TabletData(tablet.getLogin(), tablet.getLoginPassword(), tablet.getLogoutPassword()));
            }
        }
        session.removeAttribute(SESSION_ATR_TABLETS);
        status.setComplete();
        model.clear();
        return "redirect:/company/" + companyId + "/profile";
    }

    @RequestMapping(value = "/cancel",method = RequestMethod.GET)
    public String processCancel(ModelMap model, HttpSession session, @PathVariable Integer companyId) {
        model.clear();
        session.removeAttribute(SESSION_ATR_TABLETS);
        return "redirect:/company/" + companyId + "/profile";
    }

    @ModelAttribute("tablets")
    public List<TabletDto> tabletsList(HttpSession session, @PathVariable Integer pointId) {
          
         List<TabletDto> tablets = new ArrayList<TabletDto>();
         if (pointId > NEW_RECORD_ID) {
              for (Tablet t : tabletService.getTablets(pointId, Pager.ALL_RECORDS)) {
                  tablets.add(new TabletDto(t.getId(), t.getLogin(), t.getLoginPassword(), t.getLogoutPassword()));
              }   
         } else {
             tablets = (session.getAttribute(SESSION_ATR_TABLETS) != null) ? (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS) : new ArrayList<TabletDto>();
             Long i = 0L;
             for (TabletDto t : tablets) {
                 t.setId(i);
                 i++;
             }
         }
         return tablets;
    }

    @ModelAttribute("companyName")
    public String companyName(@PathVariable Integer companyId) {

        String name = "New Company";
        if (companyId > NEW_RECORD_ID) {
            name = companyService.getCompany(companyId).getName();
        }
        return name;
    }

    @RequestMapping(value = "/tablet/{tabletId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteTablet(HttpSession session, @PathVariable Integer companyId, @PathVariable Integer pointId,
            @PathVariable Long tabletId) {

        ModelMap response = new ModelMap();
        try {
            if (pointId > NEW_RECORD_ID) {
                tabletService.deleteTablet(tabletId);
            } else {
                List<TabletDto> tablets = (session.getAttribute(SESSION_ATR_TABLETS) != null) ? (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS) : new ArrayList<TabletDto>();
                tablets.remove(tabletId.intValue());
                session.setAttribute(SESSION_ATR_TABLETS, tablets);
            }
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Not deleted. Server error.");
        }
        return response;
    }

    @RequestMapping(value = "/tablet", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addTablet(HttpSession session, @PathVariable Integer companyId, @PathVariable Integer pointId,
            @ModelAttribute("tablet") TabletDto tablet) {

        ModelMap response = new ModelMap();
        try {
            if (StringUtils.isBlank(tablet.getLogin()) ||
                    StringUtils.isBlank(tablet.getLoginPassword()) ||
                    StringUtils.isBlank(tablet.getLogoutPassword())) {
                response.put("error", messageTemplateService.getMessage("dto.not.empty"));
                return response;
            }
            if (tablet.getLogin().length() > MAX_LENGTH_FIELD_TABLET ||
                    tablet.getLoginPassword().length() > MAX_LENGTH_FIELD_TABLET ||
                    tablet.getLogoutPassword().length() > MAX_LENGTH_FIELD_TABLET) {
                response.put("error", messageTemplateService.getMessage("dto.max.length", MAX_LENGTH_FIELD_TABLET.toString()));
                return response;
            }

            Map<String, Object> data = new HashMap<String, Object>();
            if (pointId > NEW_RECORD_ID) {
                Tablet newTablet = tabletService.createTablet(pointId, new TabletData(tablet.getLogin(), tablet.getLoginPassword(), tablet.getLogoutPassword()));
                data.put("id", newTablet.getId());
            } else {
                List<TabletDto> tablets = (session.getAttribute(SESSION_ATR_TABLETS) != null) ? (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS) : new ArrayList<TabletDto>();
                tablets.add(tablet);
                session.setAttribute(SESSION_ATR_TABLETS, tablets);
                data.put("id", tablets.indexOf(tablet));
            }
            data.put("login", tablet.getLogin());
            data.put("loginPassword", tablet.getLoginPassword());
            data.put("logoutPassword", tablet.getLogoutPassword());
            response.put("data", data);
            response.put("success", true);
        } catch (Exception e) {
            log.error(e, e);
            response.put("success", false);
            response.put("error", "Not added. Server error.");
        }
        return response;
    }



    private class PointProfileValidator implements org.springframework.validation.Validator {

        private final Byte LENGTH_PHONE = 20;
        private final Byte LENGTH_EMAIL = 40;
        private final Byte LENGTH_POSTAL_CODE = 40;
        private final Byte LENGTH_ADDRESS_FIELD = 80;
        private final Validator mailValidator = new EmailValidator();
        private final Validator phoneValidator = new PhoneValidator();

        @Override
        public boolean supports(Class<?> clazz) {
            return PointDto.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            /*
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone",
                    "dto.not.empty", "Please fill in the required fields.");
            
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
                    "dto.not.empty", "Please fill in the required fields.");
            */
            PointDto point = (PointDto) target;

            if (!mailValidator.isValid(point.getEmail())) {
                errors.rejectValue("email", "dto.not.valid", "Please enter valid field.");
            }
            if (!phoneValidator.isValid(point.getPhone())) {
                errors.rejectValue("phone", "dto.not.valid", "Please enter valid field.");
            }
            if (point.getPhone() != null && point.getPhone().length() > LENGTH_PHONE) {
                errors.rejectValue("phone", "dto.max.length", new Byte[]{LENGTH_PHONE} ,"Maximum length allowed is XX symbols.");
            }
            if (point.getEmail() != null && point.getEmail().length() > LENGTH_EMAIL) {
                errors.rejectValue("email", "dto.max.length",new Byte[]{LENGTH_EMAIL},"Maximum length allowed is XX symbols.");
            }
            if (point.getPostalCode() != null && point.getPostalCode().length() > LENGTH_POSTAL_CODE) {
                errors.rejectValue("postalCode", "dto.max.length", new Byte[]{LENGTH_POSTAL_CODE},"Maximum length allowed is XX symbols.");
            }
            if (point.getCity() != null && point.getCity().length() > LENGTH_ADDRESS_FIELD) {
                errors.rejectValue("city", "dto.max.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is XX symbols.");
            }
            if (point.getState() != null && point.getState().length() > LENGTH_ADDRESS_FIELD) {
                errors.rejectValue("state", "dto.max.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is XX symbols.");
            }
            if (point.getCountry() != null && point.getCountry().length() > LENGTH_ADDRESS_FIELD) {
                errors.rejectValue("country", "dto.max.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is XX symbols.");
            }
            if (point.getAddressLine1() != null && point.getAddressLine1().length() > LENGTH_ADDRESS_FIELD) {
                errors.rejectValue("addressLine1", "dto.max.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is XX symbols.");
            }
            if (point.getAddressLine2() != null && point.getAddressLine2().length() > LENGTH_ADDRESS_FIELD) {
                errors.rejectValue("addressLine2", "dto.max.length", new Byte[]{LENGTH_ADDRESS_FIELD},"Maximum length allowed is XX symbols.");
            }
        }
    }
}
