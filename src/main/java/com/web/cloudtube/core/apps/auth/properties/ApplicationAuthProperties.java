package com.web.cloudtube.core.apps.auth.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-modules.yml")
@ConfigurationProperties("cloudtube.applications.auth")
public class ApplicationAuthProperties {
    @Value("${auth_session_expiry}")
    private String authSessionExpiry;

    public String getAuthSessionExpiry() {
        return authSessionExpiry;
    }

    public void setAuthSessionExpiry(String authSessionExpiry) {
        this.authSessionExpiry = authSessionExpiry;
    }
}
