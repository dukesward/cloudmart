package com.web.cloudtube.core.security.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.web.cloudtube.core.security.test.SpringTestContextExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.List;

@ExtendWith(SpringTestContextExtension.class)
public class WebSecurityConfigurationTest {
    private static final String BEAN_PACKAGE_PREFIX = "org.springframework.security.web";
    public final AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext();

    @BeforeEach
    public void setup() {
        spring.scan("com.web.cloudtube.core");
        spring.refresh();
    }

    @Test
    public void validSecurityChainLoading() {
        SecurityFilterChain securityFilterChain = (SecurityFilterChain) spring.getBean("customSecurityFilterChain");
        Assertions.assertEquals(securityFilterChain.getClass().getCanonicalName(),
                BEAN_PACKAGE_PREFIX + ".DefaultSecurityFilterChain");
    }

    @Test
    public void validAnyAuthorizedRequests() {
        DefaultSecurityFilterChain securityFilterChain = (DefaultSecurityFilterChain) spring.getBean("customSecurityFilterChain");
        RequestMatcher requestMatcher = securityFilterChain.getRequestMatcher();
        Assertions.assertEquals(requestMatcher.getClass().getName(),
                BEAN_PACKAGE_PREFIX + ".util.matcher.AnyRequestMatcher");
    }

    @Test
    public void validAnonymousRole() {
        FilterSecurityInterceptor securityInterceptor = null;
        DefaultSecurityFilterChain securityFilterChain = (DefaultSecurityFilterChain) spring.getBean("customSecurityFilterChain");
        List<Filter> filters = securityFilterChain.getFilters();
        for(Filter filter : filters) {
            if(filter instanceof FilterSecurityInterceptor) {
                securityInterceptor = (FilterSecurityInterceptor) filter;
            }
        }
        Assertions.assertNotNull(securityInterceptor, "FilterSecurityInterceptor does not exist in filter chain");
        Collection<ConfigAttribute> attributes = securityInterceptor.obtainSecurityMetadataSource().getAllConfigAttributes();
        Assertions.assertEquals(attributes.size(), 1);
        // expect to be WebExpressionConfigAttribute however this class is not visible to tester
        for (ConfigAttribute attribute : attributes) {
            String _attribute = attribute.toString();
            Assertions.assertEquals(_attribute, "hasRole('ROLE_VISITOR')");
        }
    }
}
