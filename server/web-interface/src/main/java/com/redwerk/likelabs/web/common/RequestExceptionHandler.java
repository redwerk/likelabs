package com.redwerk.likelabs.web.common;

import com.redwerk.likelabs.domain.model.review.exception.ReviewNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component(value="exceptionResolver")
public class RequestExceptionHandler implements HandlerExceptionResolver {

    private final Logger log = LogManager.getLogger(getClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception exception) {

        ModelAndView mav;
        if (exception instanceof ReviewNotFoundException) {
            mav = new ModelAndView("error/content_not_found");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            log.error("Bad request error: " + exception.getMessage() , exception);
            return mav;
        }
        if (exception instanceof AccessDeniedException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            log.error("Access level error: " + exception.getMessage() , exception);
            //processed further to AccessDeniedHandler
            return null;
        }
        mav = new ModelAndView("error/server-error");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("Internal error: " + exception.getMessage() , exception);
        return mav;
    }
}
