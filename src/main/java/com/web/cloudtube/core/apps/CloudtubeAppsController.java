package com.web.cloudtube.core.apps;

import com.web.cloudtube.core.security.auth.ApplicationBasicPolicy;
import com.web.cloudtube.core.security.auth.ApplicationSecurityPolicySupplier;

import java.util.List;

public abstract class CloudtubeAppsController {
    protected static final String HTTP_RESPONSE_OK = "success";
    protected ApplicationSecurityPolicySupplier policySupplier;
    public List<ApplicationBasicPolicy> getPolicies() {
        return this.policySupplier.getPolicies();
    }

}
