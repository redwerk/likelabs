package com.redwerk.likelabs.web.rest.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redwerk.likelabs.application.TabletService;
import com.redwerk.likelabs.domain.model.tablet.exception.TabletNotFoundException;
import com.redwerk.likelabs.web.rest.resource.mapping.LinkBuilder;

@Component("tabletSecurityFilter")
public class TabletSecurityFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(TabletSecurityFilter.class);

    public static final String HEADER_TABLET_API_KEY = "tablet-api-key";

    private static final Pattern TABLET_ID_URL_PATTERN = Pattern.compile("/tablets/(\\d+)(?:/.*)*");

    @Autowired
    private TabletService tabletService;

    private final Set<String> bypassedPaths = new HashSet<String>() {{
        add("/");
        add(null);  //root restapi path
        add(LinkBuilder.Url.LOGIN);
    }};

    @Override
    public void destroy() {
        // do nothing;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //unluckily this is not called by Spring's DelegatingFilterProxy
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest) || !(resp instanceof HttpServletResponse)) {
            throw new ServletException("TabletSecurityFilter supports only HTTP requests");
        }
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;      
        LOG.debug("got request to filter: {}", request);
        String apiKey = request.getHeader(HEADER_TABLET_API_KEY);
        
        if(!bypassedPaths.contains(request.getPathInfo())) {
            if (apiKey == null) {
                createUnauthorizedResponse(response);
                return;  
            }
            Long tabletId = getTabletId(request);
            if(tabletId == null) {
                createNotFoundResponse(response);
                return;
            }
            try {
                if(!tabletService.apiKeyIsValid(tabletId, apiKey)) {
                    createUnauthorizedResponse(response);
                    return;
                }
            } catch (TabletNotFoundException ex) {
                createNotFoundResponse(response);
                return;  		
            }
 
        }
        LOG.debug("resolution: request allowed");
        chain.doFilter(request, response);
    }

    private void createUnauthorizedResponse(HttpServletResponse response) {
        LOG.debug("resolution: client unauthorized");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
    }

    private void createNotFoundResponse(HttpServletResponse response) {
        LOG.debug("resolution: resource not found");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND); 
    }

    private Long getTabletId(HttpServletRequest request) {
        Matcher matcher = TABLET_ID_URL_PATTERN.matcher(request.getPathInfo());
        Long tabletId = null;
        if(matcher.find() && matcher.groupCount() > 0) {
            tabletId = Long.parseLong(matcher.group(1));
        }
        return tabletId;
    }
}