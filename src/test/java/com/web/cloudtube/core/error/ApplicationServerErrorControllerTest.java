package com.web.cloudtube.core.error;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
public class ApplicationServerErrorControllerTest {

    public final AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext();

    @BeforeEach
    public void setup() {

    }

    @Test
    void testRequestErrorRedirect() {

    }
}
