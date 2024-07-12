package com.web.cloudtube.core.validation;

import java.util.Map;

public interface RequestBodyValidator extends ApplicationObjectValidator<Map<String, Object>> {
    public ValidationResult validate(Map<String, Object> payload);
}
