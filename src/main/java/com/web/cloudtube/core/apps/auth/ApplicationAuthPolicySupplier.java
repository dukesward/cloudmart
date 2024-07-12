package com.web.cloudtube.core.apps.auth;

import com.web.cloudtube.core.security.auth.ApplicationBasicPolicy;
import com.web.cloudtube.core.security.auth.ApplicationSecurityPolicySupplier;
import org.springframework.http.HttpMethod;

public class ApplicationAuthPolicySupplier extends ApplicationSecurityPolicySupplier {

    @Override
    protected void initPolicies() {
        this.policies.add(customerProfilePolicy());
        this.policies.add(processLoginPolicy());
        this.policies.add(processRegisterPolicy());
    }

    private ApplicationBasicPolicy customerProfilePolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("customerProfile");
        policy.method(HttpMethod.GET.toString()).grantAll("/cloudtube/getCustomerProfile");
        return policy;
    }

    private ApplicationBasicPolicy processLoginPolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("processLogin");
        policy.method(HttpMethod.POST.toString()).grantAll("/cloudtube/processLogin");
        return policy;
    }

    private ApplicationBasicPolicy processRegisterPolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("processRegister");
        policy.method(HttpMethod.POST.toString()).grantAll("/cloudtube/processRegister");
        return policy;
    }
}
