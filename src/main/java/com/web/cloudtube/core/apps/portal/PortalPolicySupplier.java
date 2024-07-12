package com.web.cloudtube.core.apps.portal;


import com.web.cloudtube.core.security.auth.ApplicationBasicPolicy;
import com.web.cloudtube.core.security.auth.ApplicationSecurityPolicySupplier;
import org.springframework.http.HttpMethod;

public class PortalPolicySupplier extends ApplicationSecurityPolicySupplier {
    @Override
    protected void initPolicies() {
        this.policies.add(getWelcomeMessagePolicy());
    }

    private ApplicationBasicPolicy getWelcomeMessagePolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("welcomeMessage");
        policy.method(HttpMethod.GET.toString()).grantAll("/cloudtube/dsd/welcome");
        return policy;
    }

}
