package com.web.cloudtube.core.security.auth;

import com.web.cloudtube.core.apps.BaseProfile;
import com.web.cloudtube.core.apps.UserBaseProfile;
import com.web.cloudtube.core.apps.auth.UserGroup;
import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import com.web.cloudtube.core.security.entity.PolicyRule;
import com.web.cloudtube.core.security.entity.PolicyRuleProcessor;
import com.web.cloudtube.core.security.service.ApplicationPolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApplicationPolicyRuleFilter extends GenericFilterBean {
    Logger logger = LoggerFactory.getLogger(ApplicationPolicyRuleFilter.class);
    private static final String USERTYPE_ALL = "ALL";
    private final ApplicationContext context;

    public ApplicationPolicyRuleFilter(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String sessionId = request.getRequestedSessionId();
        if(sessionId == null && SecurityContextHolder.getContext().getAuthentication() != null) {
            WebAuthenticationDetails details =
                    (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
            sessionId = details.getSessionId();
        }
        UserBaseProfile baseProfile = null;
        int status = 0;
        if(sessionId != null) {
            baseProfile = (UserBaseProfile) BaseProfile.getUserProfile(sessionId);
            ApplicationBasicPolicy policy = (ApplicationBasicPolicy) baseProfile.getProperty("policy");
            if(policy == null) {
                logger.error("Not able to find policy for target route: " + request.getRequestURI());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Policy not found");
            }
            if(!policy.getRule().isA(USERTYPE_ALL, PolicyRule.PolicyType.GRANT)) {
                PolicyRuleProcessor processor = new PolicyRuleProcessor(policy.getRule());
                PolicyRule rule = policy.getRule();
                CustomerSessionData sessionData = (CustomerSessionData) baseProfile.getProperty("customerSessionData");
                if(!rule.isA(sessionData.getUserType(), PolicyRule.PolicyType.GRANT)) {
                    if(rule.getUserGroup().includesType(UserGroup.UserType.fromType(sessionData.getUserType()))) {
                        try {
                            PolicyRule.PolicyCondition condition = rule.getCondition();
                            boolean result = false;
                            switch (condition.getPolicyType()) {
                                default:
                                case GRANT_ON:
                                    if(!satisfies(request, response, filterChain, condition, policy.getHandler())) {
                                        //policy.getHandler().handle(request, response, filterChain, result);
                                        status = 1;
                                    }
                                    break;
                                case REJECT_ON:
                                    if(satisfies(request, response, filterChain, condition, policy.getHandler())) {
                                        //policy.getHandler().handle(request, response, filterChain, result);
                                        status = 1;
                                    }
                                    break;
                            }
                        }catch (Exception e) {
                            logger.error("Error processing policy for: " + request.getRequestURI());
                            throw new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR, "Error processing policy", e);
                        }
                    }else {
                        //
                    }
                }
            }
            if(status == 1) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Access rejected by policy");
            }
            try {
                filterChain.doFilter(request, response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            logger.error("Session is invalid");
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Invalid customer session");
        }
    }

    private boolean satisfies(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            PolicyRule.PolicyCondition condition,
            ApplicationBasicPolicy.PolicyHandlerFunction handler) throws Exception {
        String ruleName = condition.getRuleName();
        String[] tokens = ruleName.split(":");
        if(tokens.length == 2) {
            String service = tokens[0];
            String action = tokens[1];
            try {
                ApplicationPolicyService invoker = (ApplicationPolicyService) context.getBean(service);
                boolean result = invoker.action(action);
                return handler.handle(request, response, filterChain, result);
            }catch (Exception e) {
                logger.error("Error handling action for rule " + ruleName + ": " + e.getMessage());
                throw e;
            }
        }else {
            return false;
        }
    }

    private boolean satisfies(PolicyRule.PolicyCondition condition, boolean last) {
        String ruleName = condition.getRuleName();
        return true;
    }
}
