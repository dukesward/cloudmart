package com.web.cloudtube.core.validation;

public class ApplicationObjectValidationStep<T> {
    private final ApplicationObjectValidator<T> validator;
    private ApplicationObjectValidationStep<T> next;

    public ApplicationObjectValidationStep(
            ApplicationObjectValidator<T> validator) {
        this.validator = validator;
    }

    public ApplicationObjectValidationStep<T> linkWith(ApplicationObjectValidator<T> next) {
        ApplicationObjectValidationStep<T> step = this;
        while(step.next != null) {
            step = step.next;
        }
        step.next = new ApplicationObjectValidationStep<>(next);
        return this;
    }

    public ValidationResult validate(T target) {
        ValidationResult result = this.validator.validate(target);
        return result.isValid() ? (this.next == null ? result : this.next.validate(target)) : result;
    }
}
