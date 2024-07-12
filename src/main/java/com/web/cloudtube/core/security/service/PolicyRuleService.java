package com.web.cloudtube.core.security.service;

import com.web.cloudtube.core.security.entity.PolicyRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolicyRuleService {
    static final Logger logger = LoggerFactory.getLogger(PolicyRuleService.class);
    static final String VISITOR = "visitor";
    static final String CUSTOMER = "customer";

    public PolicyRuleService() {}

    public PolicyRule grantAllRule(String policyId) {
        return PolicyRule.grantAll(policyId);
    }

    public PolicyRule customerOnlyRule(String policyId) {
        return PolicyRule.grant(policyId, CUSTOMER);
    }

    public PolicyRule rejectVisitorRule(String policyId, String ruleName) {
        return PolicyRule.rejectOn(policyId, VISITOR).withRule(ruleName);
    }

    public PolicyRule rejectAllRule(String policyId) {
        return PolicyRule.rejectAll(policyId);
    }
}
