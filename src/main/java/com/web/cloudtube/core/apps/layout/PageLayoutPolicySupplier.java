package com.web.cloudtube.core.apps.layout;

import com.web.cloudtube.core.security.auth.ApplicationBasicPolicy;
import com.web.cloudtube.core.security.auth.ApplicationSecurityPolicySupplier;
import org.springframework.http.HttpMethod;

public class PageLayoutPolicySupplier extends ApplicationSecurityPolicySupplier {
    @Override
    protected void initPolicies() {
        this.policies.add(pageMetadataPolicy());
        this.policies.add(pageContentPolicy());
    }

    private ApplicationBasicPolicy pageMetadataPolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("metadata");
        policy.method(HttpMethod.GET.toString()).grantAll("/cloudtube/metadata/**");
        return policy;
    }

    private ApplicationBasicPolicy pageContentPolicy() {
        ApplicationBasicPolicy policy = new ApplicationBasicPolicy("content");
        policy.method(HttpMethod.GET.toString()).grantAll("/cloudtube/content/**");
        return policy;
    }
}
