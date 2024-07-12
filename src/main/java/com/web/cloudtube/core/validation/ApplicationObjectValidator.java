package com.web.cloudtube.core.validation;

public interface ApplicationObjectValidator<T> {

    public ValidationResult validate(T target);
}
