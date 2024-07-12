package com.web.cloudtube.core.apps.auth;

import com.web.cloudtube.core.validation.service.ApplicationObjectValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class CloudtubeCustomerProfileControllerTest {

    public final AnnotationConfigApplicationContext context
            = new AnnotationConfigApplicationContext();

    @BeforeEach
    public void setup() {
        ConfigurableListableBeanFactory beanFactory = this.context.getBeanFactory();
        context.refresh();
    }

    @Test
    void testSecurityFilterCreation() {

    }

    @Test
    void testValidator() {
        ApplicationObjectValidationService<Map<String, Object>> validationService
                = new ApplicationAuthRequestValidatorSupplier();
        Method[] methods = validationService.getClass().getDeclaredMethods();
        System.out.println(Arrays.toString(methods));
    }
}
