package com.web.cloudtube.core.security.auth;

import java.util.ArrayList;
import java.util.List;

public abstract class ApplicationSecurityPolicySupplier {
    protected List<ApplicationBasicPolicy> policies;
    public ApplicationSecurityPolicySupplier() {
        this.policies = new ArrayList<ApplicationBasicPolicy>();
        this.initPolicies();
    }
    protected abstract void initPolicies();
    public List<ApplicationBasicPolicy> getPolicies() {
        return policies;
    };
}
