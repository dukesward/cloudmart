package com.web.cloudtube.core.apps.auth;

import com.web.cloudtube.core.validation.ApplicationObjectValidationStep;
import com.web.cloudtube.core.validation.RequestBodyValidator;
import com.web.cloudtube.core.validation.ValidationResult;
import com.web.cloudtube.core.validation.service.ApplicationObjectValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ApplicationAuthRequestValidatorSupplier
        extends ApplicationObjectValidationService<Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationAuthRequestValidatorSupplier.class);
    private static final Map<String, RequestBodyValidator> validators = new HashMap<>();

    protected ValidationResult validateEmail(Map<String, Object> target) {
        String key = "email";
        Object email = target.get(key);
        if(email == null) {
            return ValidationResult.result(key, ValidationResult.ValidationResultCode.NULL_VALUE);
        }
        if(!(email instanceof String) || ((String) email).length() <= 5 || ((String) email).indexOf('@') <= 0) {
            return ValidationResult.result(key, ValidationResult.ValidationResultCode.INVALID_VALUE);
        }
        return ValidationResult.succeed(key);
    }

    protected ValidationResult validatePassword(Map<String, Object> target) {
        String key = "password";
        Object password = target.get(key);
        if(password == null) {
            return ValidationResult.result(key, ValidationResult.ValidationResultCode.NULL_VALUE);
        }
        if(!(password instanceof String)
                || ((String) password).length() < 6
                || ((String) password).length() > 50) {
            return ValidationResult.result(key, ValidationResult.ValidationResultCode.INVALID_VALUE);
        }
        return ValidationResult.succeed(key);
    }

    public ValidationResult validateProcessEmailLogin(Map<String, Object> target) {
        return new ApplicationObjectValidationStep<Map<String, Object>>(this.applyCommonValidator())
                .linkWith(this::validateEmail)
                .linkWith(this::validatePassword)
                .validate(target);
    }

    public ValidationResult validateProcessEmailRegister(Map<String, Object> target) {
        return new ApplicationObjectValidationStep<Map<String, Object>>(this.applyCommonValidator())
                .linkWith(this::validateEmail)
                .linkWith(this::validatePassword)
                .validate(target);
    }
}
