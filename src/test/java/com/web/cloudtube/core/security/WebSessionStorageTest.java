package com.web.cloudtube.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class WebSessionStorageTest {

    private static final String BEAN_PACKAGE_PREFIX = "org.springframework.security.web";
    public final AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext();

    @BeforeEach
    public void setup() {
        spring.scan("com.web.cloudtube.core");
        spring.refresh();
    }

    @Test
    public void validateSessionCleanup() {

    }
}
