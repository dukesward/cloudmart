package com.web.cloudtube.core.backend;

import com.web.cloudtube.core.error.resolver.HttpErrorResponseWrapperResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ApplicationMVCConfigurer implements WebMvcConfigurer {
    @Autowired
    private HttpErrorResponseWrapperResolver errorResponseWrapperResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        if(this.errorResponseWrapperResolver != null) {
            resolvers.add(this.errorResponseWrapperResolver);
        }
    }
}
