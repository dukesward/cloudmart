package com.web.cloudtube.core.error;

import com.web.cloudtube.core.security.auth.ApplicationBasicPolicy;
import com.web.cloudtube.core.security.auth.ApplicationSecurityPolicySupplier;
import org.springframework.http.HttpMethod;

public class ApplicationErrorPagePolicySupplier extends ApplicationSecurityPolicySupplier {

    @Override
    protected void initPolicies() {
        this.policies.add(serverInternalErrorPolicy());
    }

    private ApplicationBasicPolicy serverInternalErrorPolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("serverError");
        policy.method(HttpMethod.GET.toString()).grantAll("/error");
        return policy;
    }
}
