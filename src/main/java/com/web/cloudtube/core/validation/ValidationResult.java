package com.web.cloudtube.core.validation;

public class ValidationResult {
    private final ValidationResultCode resultCode;
    private final String key;

    private ValidationResult(
            String key, ValidationResultCode resultCode) {
        this.resultCode = resultCode;
        this.key = key;
    }

    public static ValidationResult succeed(String key) {
        return new ValidationResult(key, ValidationResultCode.VALID);
    }

    public static ValidationResult result(String key, ValidationResultCode resultCode) {
        return new ValidationResult(key, resultCode);
    }

    public boolean isValid() {
        return this.resultCode == ValidationResultCode.VALID;
    }

    public ValidationResult or(ValidationResult result) {
        return this.isValid() ? result : this;
    }

    public ValidationResultCode getResultCode() {
        return resultCode;
    }

    public enum ValidationResultCode {
        VALID,

        NULL_VALUE,

        INVALID_VALUE
    }
}
