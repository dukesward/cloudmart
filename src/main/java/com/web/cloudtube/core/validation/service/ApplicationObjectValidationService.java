package com.web.cloudtube.core.validation.service;

import com.web.cloudtube.core.util.CloseableReflectionMethodHandler;
import com.web.cloudtube.core.validation.ApplicationObjectValidator;
import com.web.cloudtube.core.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class ApplicationObjectValidationService<T> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationObjectValidationService.class);
    public ValidationResult validate(String validator, T target) {
        validator = "validate" + validator.substring(0, 1).toUpperCase() + validator.substring(1);
        try (CloseableReflectionMethodHandler handler
                     = new CloseableReflectionMethodHandler(validator, this.getClass())) {
            return (ValidationResult) handler.getMethod().invoke(this, target);
        } catch (NoSuchMethodException e) {
            logger.error("Validator method not found: " + validator);
            return null;
        } catch (Exception e) {
            logger.error("Error invoking validator method: " + validator);
            throw new RuntimeException(e);
        }
    }

    public ApplicationObjectValidator<T> applyCommonValidator() {
        return (target) -> {
            String key = "target";
            return target != null ?
                    ValidationResult.succeed(key) :
                    ValidationResult.result(key, ValidationResult.ValidationResultCode.NULL_VALUE);
        };
    }
}
