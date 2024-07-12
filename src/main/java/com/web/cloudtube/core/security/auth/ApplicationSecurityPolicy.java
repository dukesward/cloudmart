package com.web.cloudtube.core.security.auth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import javax.servlet.http.HttpServletRequest;

public interface ApplicationSecurityPolicy {
    public void configureAuthorize(HttpSecurity http) throws Exception;
    public ApplicationSecurityPolicy method(String httpMethod);
    public boolean matches(HttpServletRequest request);
    public void grantAll(String route);
    public void denyAll(String route);
}
