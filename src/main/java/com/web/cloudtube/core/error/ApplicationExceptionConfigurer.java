package com.web.cloudtube.core.error;

import com.web.cloudtube.core.error.resolver.ApplicationServerExceptionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApplicationExceptionConfigurer implements WebMvcConfigurer {

    @Override
    public void configureHandlerExceptionResolvers(
            List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, new ApplicationServerExceptionResolver());
    }
}
