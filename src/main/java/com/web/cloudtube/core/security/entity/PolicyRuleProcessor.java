package com.web.cloudtube.core.security.entity;

public class PolicyRuleProcessor {
    private PolicyRule rule;

    public PolicyRuleProcessor(PolicyRule rule) {
        this.rule = rule;
    }

    public PolicyRule getRule() {
        return rule;
    }

    public void setRule(PolicyRule rule) {
        this.rule = rule;
    }

}
