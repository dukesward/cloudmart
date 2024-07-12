package com.web.cloudtube.core.security.auth;

import com.web.cloudtube.core.apps.BaseProfile;
import com.web.cloudtube.core.apps.UserBaseProfile;
import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import com.web.cloudtube.core.apps.auth.service.CustomerLoginService;
import com.web.cloudtube.core.apps.auth.service.CustomerProfileService;
import com.web.cloudtube.core.error.throwable.CustomerInvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ApplicationSecurityFilter extends GenericFilterBean implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(ApplicationSecurityFilter.class);

    private final List<ApplicationBasicPolicy> policies;
    private final CustomerProfileService customerProfileService;

    public ApplicationSecurityFilter(
            List<ApplicationBasicPolicy> policies,
            CustomerProfileService customerProfileService
    ) {
        this.policies = policies;
        this.customerProfileService = customerProfileService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String sessionId = request.getRequestedSessionId();
        if(sessionId == null && SecurityContextHolder.getContext().getAuthentication() != null) {
            WebAuthenticationDetails details =
                    (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
            sessionId = details.getSessionId();
        }
        UserBaseProfile baseProfile = null;
        try {
            if(this.customerProfileService != null && sessionId != null) {
                baseProfile = (UserBaseProfile) BaseProfile.getUserProfile(sessionId);
                baseProfile.setProperty("authType", request.getAuthType());
                baseProfile.setProperty("sessionId", sessionId);
                for(ApplicationBasicPolicy policy: this.policies) {
                    if(policy.matches(request)) {
                        logger.debug("found correct policy " + policy);
                        baseProfile.setProperty("policy", policy);
                        break;
                    }
                }
                baseProfile.setProperty("httpRemoteAddress", this.getIpAddress(request));
                CustomerSessionData sessionData = this.customerProfileService.loadSessionData(request, response, baseProfile);
                if (sessionData != null) {
                    baseProfile.setProperty("customerSessionData", sessionData);
                }
            }
            filterChain.doFilter(request, response);
        }catch (CustomerInvalidSessionException e) {
            logger.error("Customer session is invalid");
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Invalid customer session", e);
        }catch (Exception e) {
            logger.error("Sever internal error due to " + e.getMessage());
            throw e;
        }finally {
            UserBaseProfile.cleanup();
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String sourceIp = null;
        String ipAddresses = request.getHeader("x-forwarded-for");
        if (StringUtils.hasText(ipAddresses)) {
            sourceIp = ipAddresses.split(",")[0];
        }else {
            sourceIp = request.getRemoteAddr();
        }
        return sourceIp;
    }
}
