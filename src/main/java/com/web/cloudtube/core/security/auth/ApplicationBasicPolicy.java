package com.web.cloudtube.core.security.auth;

import com.web.cloudtube.core.security.entity.PolicyRule;
import com.web.cloudtube.core.security.service.PolicyRuleService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApplicationBasicPolicy {
    private final String policyId;
    private String path;
    private String httpMethod;
    private PolicyRule rule;
    private final PolicyRuleService policyRuleService;

    private PolicyHandlerFunction handler;

    public ApplicationBasicPolicy(String policyId) {
        this.policyId = policyId;
        this.policyRuleService = new PolicyRuleService();
    }
    public String getPolicyId() {
        return policyId;
    }

    public PolicyRule getRule() { return rule; }

    public String getPath() { return path; }

    public String getHttpMethod() { return httpMethod; }

    public PolicyHandlerFunction getHandler() {
        return handler;
    }

    public void setHandler(PolicyHandlerFunction handler) {
        this.handler = handler;
    }

    /*
     * we will use policy rule only to define conditions for
     * authorization and thus this method can safely be taken down
     */
    @Deprecated
    public void configureAuthorize(HttpSecurity http) throws Exception {
        if(!"".equals(this.path) && this.rule != null) {
            http.authorizeRequests((requests) -> requests.antMatchers(this.path).hasRole(this.rule.getUserType()));
        }
    }

    public ApplicationBasicPolicy method(String httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public boolean matches(HttpServletRequest request) {
        RequestMatcher matcher = new AntPathRequestMatcher(this.path, this.httpMethod);
        return matcher.matches(request);
    }

    protected void addVisitorPolicy(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) -> requests.antMatchers(this.path).hasRole(this.rule.getUserType()));
    }

    public void grantCustomerOnly(String route) {
        this.path = route;
        this.rule = this.policyRuleService.customerOnlyRule(this.policyId);
    }

    public void grantAll(String route) {
        this.path = route;
        this.rule = this.policyRuleService.grantAllRule(this.policyId);
    }

    public void rejectVisitor(String route, PolicyHandlerFunction handler) {
        this.path = route;
        this.rule = this.policyRuleService
                .rejectVisitorRule(this.policyId, "customerLoginService:processLogin");
        this.handler = handler;
    }

    public void rejectAll(String route) {
        this.path = route;
        this.rule = this.policyRuleService.rejectAllRule(this.policyId);
    }

    public interface PolicyHandlerFunction {
        public boolean handle(HttpServletRequest request, HttpServletResponse response,
                           FilterChain filterChain, boolean result);
    }
}
