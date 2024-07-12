package com.web.cloudtube.core.security.entity;

import com.web.cloudtube.core.apps.auth.UserGroup;
import org.springframework.security.core.parameters.P;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

public class PolicyRule {

    private String policyId;
    private UserGroup userGroup;
    private PolicyCondition condition;
    private String externalRule;

    private PolicyRule(String policyId, UserGroup userGroup) {
        this.policyId = policyId;
        this.userGroup = userGroup;
        this.condition = new PolicyCondition(PolicyType.GRANT);
    }

    public static PolicyRule grantAll(String policyId) {
        PolicyRule rule = new PolicyRule(policyId, new UserGroup("all"));
        rule.condition = new PolicyCondition(PolicyType.GRANT);
        return rule;
    }

    public static PolicyRule grant(String policyId, String userGroup) {
        PolicyRule rule = new PolicyRule(policyId, new UserGroup(userGroup));
        rule.condition = new PolicyCondition(PolicyType.GRANT);
        return rule;
    }

    public static PolicyRule rejectOn(String policyId, String userGroup) {
        PolicyRule rule = new PolicyRule(policyId, new UserGroup(userGroup));
        rule.condition = new PolicyCondition(PolicyType.REJECT_ON);
        return rule;
    }

    public static PolicyRule rejectAll(String policyId) {
        PolicyRule rule = new PolicyRule(policyId, new UserGroup("all"));
        rule.condition = new PolicyCondition(PolicyType.REJECT);
        return rule;
    }

    public boolean isA(String userGroup, PolicyType type) {
        return this.userGroup.includesType(userGroup) && this.condition.policyType == type;
    }

    public boolean isA(int userGroup, PolicyType type) {
        return userGroup == this.userGroup.getUserType() && this.condition.policyType == type;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public String getUserType() {
        return userGroup.getUserTypeValue();
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public PolicyCondition getCondition() {
        return condition;
    }

    public PolicyRule withRule(String ruleName) {
        this.condition.setRuleName(ruleName);
        return this;
    }

    public void and(PolicyType policyType) {
        if(this.condition == null) {
            this.condition = new PolicyCondition(policyType);
        }else {
            this.condition.addCondition(1, policyType);
        }
    }

    public String getExternalRule() {
        return externalRule;
    }

    public void setExternalRule(String externalRule) {
        this.externalRule = externalRule;
    }

    public static class PolicyCondition {
        private PolicyType policyType;
        private PolicyCondition nextCondition;
        private int logic = 1;
        private boolean externalRule = false;
        private String ruleName;
        public PolicyCondition(PolicyType policyType) {
            this.policyType = policyType;
        }

        public PolicyType getPolicyType() {
            return policyType;
        }

        public void setPolicyType(PolicyType policyType) {
            this.policyType = policyType;
        }

        public int getLogic() { return this.logic; }

        public String getRuleName() {
            return ruleName;
        }

        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        public void addCondition(int logic, PolicyType policyType) {
            if(this.nextCondition == null) {
                this.logic = logic;
                this.nextCondition = new PolicyCondition(policyType);
            }else {
                this.nextCondition.addCondition(logic, policyType);
            }
        }
    }

    public static class PolicyConditionResult {
        private boolean result;
        private PolicyExceptionType exceptionType = PolicyExceptionType.SUCCESS;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public PolicyExceptionType getExceptionType() {
            return exceptionType;
        }

        public void setExceptionType(PolicyExceptionType exceptionType) {
            this.exceptionType = exceptionType;
        }
    }

    public enum PolicyType {
        GRANT,
        GRANT_ON,
        GRANT_UNLESS,
        REJECT,
        REJECT_ON,
        REJECT_UNLESS;
    }

    public enum PolicyExceptionType {
        SUCCESS,
        SERVER_ERROR,
        POLICY_REJECTED
    }
}
