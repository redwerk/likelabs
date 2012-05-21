package com.redwerk.likelabs.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component(value="exceptionResolver")
public class RequestExceptionHandler implements HandlerExceptionResolver {

    private final Logger log = LogManager.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception e) {
        
        ModelAndView mav = new ModelAndView("error/server-error");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // mav.addObject("error_message", e.getMessage() + " - in" + e.getStackTrace()[0].getClassName() + " line:" + e.getStackTrace()[0].getLineNumber());
        log.error("Internal error: " + e.getMessage() , e);
        return mav;
    }
}
