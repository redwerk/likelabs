package com.redwerk.likelabs.web.ui.controller.company;

import com.redwerk.likelabs.application.CompanyService;
import com.redwerk.likelabs.application.PointService;
import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.application.dto.TabletData;
import com.redwerk.likelabs.application.template.MessageTemplateService;
import com.redwerk.likelabs.domain.model.point.Point;
import com.redwerk.likelabs.domain.model.query.Pager;
import com.redwerk.likelabs.domain.model.tablet.Tablet;
import com.redwerk.likelabs.web.ui.dto.PointDto;
import com.redwerk.likelabs.web.ui.dto.TabletDto;
import com.redwerk.likelabs.web.ui.utils.JsonResponseBuilder;
import com.redwerk.likelabs.web.ui.validator.PointProfileValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * Secure on Controller uses {@link com.redwerk.likelabs.web.ui.security.DecisionAccess}
 * All methods for mapping must have parameter companyId
 */
@PreAuthorize("@decisionAccess.permissionCompany(principal, #companyId)")
@Controller
@RequestMapping(value = "/company/{companyId}/point/{pointId}/profile")
public class PointProfileController {


    private final static int NEW_POINT_ID = 0;
    private final static String SESSION_ATR_TABLETS = "tablets";
    private final static String VIEW_POINT_PROFILE = "company/point_profile";
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
    public String initForm(ModelMap model, @PathVariable Long companyId,
                        @PathVariable Long pointId) {

        PointDto point = null;
        if (pointId > NEW_POINT_ID) {
            Point pointDomain = pointService.getPoint(pointId);
            point = new PointDto(pointDomain);
            model.addAttribute("title", "Edit Point for ");
        } else {
            point = new PointDto();
            model.addAttribute("title", "New Point for ");
        }
        model.addAttribute("point", point);
        model.addAttribute("page", "profile");
        return VIEW_POINT_PROFILE;

    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(ModelMap model, HttpSession session, @PathVariable Long companyId,
                  @PathVariable Long pointId, @ModelAttribute("point") PointDto point,
                          BindingResult result, SessionStatus status) {

        validator.validate(point, result);
        if (result.hasErrors()) {
            model.addAttribute("page", "profile");
            if (pointId > NEW_POINT_ID) {
                model.addAttribute("title", "Edit Point for ");
            } else {
                model.addAttribute("title", "New Point for ");
            }
            return VIEW_POINT_PROFILE;
        }
        if (pointId > NEW_POINT_ID) {
            pointService.updatePoint(pointId, point.getPointData());
        } else {
            Point newPoint = pointService.createPoint(companyId, point.getPointData());
            List<TabletDto> newTablets = (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS);
            if (newTablets != null) {
                for (TabletDto tablet: newTablets) {
                    tabletService.createTablet(newPoint.getId(),
                            new TabletData(tablet.getLogin(), tablet.getLoginPassword(), tablet.getLogoutPassword()));
                }
            }
        }
        session.removeAttribute(SESSION_ATR_TABLETS);
        status.setComplete();
        model.clear();
        return "redirect:/company/" + companyId + "/profile";
    }

    @RequestMapping(value = "/cancel",method = RequestMethod.GET)
    public String processCancel(ModelMap model, HttpSession session, @PathVariable Long companyId) {
        model.clear();
        session.removeAttribute(SESSION_ATR_TABLETS);
        return "redirect:/company/" + companyId + "/profile";
    }

    @ModelAttribute("tablets")
    public List<TabletDto> tabletsList(HttpSession session, @PathVariable Long companyId, @PathVariable Long pointId) {
          
         List<TabletDto> tablets = new ArrayList<TabletDto>();
         if (pointId > NEW_POINT_ID) {
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
    public String companyName(@PathVariable Long companyId) {

        return companyService.getCompany(companyId).getName();
    }

    @RequestMapping(value = "/tablet/{tabletId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ModelMap deleteTablet(HttpSession session, @PathVariable Long companyId, @PathVariable Long pointId,
            @PathVariable Long tabletId) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            if (pointId > NEW_POINT_ID) {
                tabletService.deleteTablet(tabletId);
            } else {
                List<TabletDto> tablets = (session.getAttribute(SESSION_ATR_TABLETS) != null) ? (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS) : new ArrayList<TabletDto>();
                tablets.remove(tabletId.intValue());
                session.setAttribute(SESSION_ATR_TABLETS, tablets);
            }
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("company.tablet.not.deleted"));
        }
        return resBuilder.getModelResponse();
    }

    @RequestMapping(value = "/tablet", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addTablet(HttpSession session, @PathVariable Integer companyId, @PathVariable Integer pointId,
            @ModelAttribute("tablet") TabletDto tablet) {

        JsonResponseBuilder resBuilder = new JsonResponseBuilder();
        try {
            List<String> errors = validator.validateTablet(tablet, messageTemplateService);
            if (!errors.isEmpty()) {
                resBuilder.addMessages(errors);
                resBuilder.addCustomFieldData("valide", false);
                return resBuilder.getModelResponse();
            }
            if (pointId > NEW_POINT_ID) {
                tabletService.createTablet(pointId, new TabletData(tablet.getLogin(), tablet.getLoginPassword(), tablet.getLogoutPassword()));
            } else {
                List<TabletDto> tablets = (session.getAttribute(SESSION_ATR_TABLETS) != null) ? (List<TabletDto>)session.getAttribute(SESSION_ATR_TABLETS) : new ArrayList<TabletDto>();
                tablets.add(tablet);
                session.setAttribute(SESSION_ATR_TABLETS, tablets);
            }
            resBuilder.addCustomFieldData("valide", true);
        } catch (Exception e) {
            log.error(e, e);
            resBuilder.setNotSuccess(messageTemplateService.getMessage("company.tablet.not.added"));
        }
        return resBuilder.getModelResponse();
    }
}
