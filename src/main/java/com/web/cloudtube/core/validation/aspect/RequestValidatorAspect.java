package com.web.cloudtube.core.validation.aspect;

import com.web.cloudtube.core.validation.ValidationResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestValidatorAspect {

    @Before("@annotation(RequestValidated)")
    public void validateRequestBody(JoinPoint joinPoint) {
        System.out.println("Enter request body validator");
    }
}
