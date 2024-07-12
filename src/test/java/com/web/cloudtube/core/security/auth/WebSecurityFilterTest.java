package com.web.cloudtube.core.security.auth;

import com.web.cloudtube.core.apps.auth.CloudtubeCustomerProfileController;
import com.web.cloudtube.core.security.test.SpringTestContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.Filter;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Configuration
@ExtendWith(SpringTestContextExtension.class)
@PropertySource("classpath:application.properties")
@PropertySource("classpath:bootstrap.yml")
public class WebSecurityFilterTest {
    private static final String BEAN_PACKAGE_PREFIX = "org.springframework.security.web";
    public final AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext();

    @BeforeEach
    public void setup() {
        spring.scan("com.web.cloudtube.core");
        spring.refresh();
    }

    @Test
    public void applicationSecurityFilter() throws Exception {
        DefaultSecurityFilterChain securityFilterChain
                = (DefaultSecurityFilterChain) spring.getBean("customSecurityFilterChain");
        List<Filter> filters = securityFilterChain.getFilters();
        ApplicationSecurityFilter securityFilter = null;
        for(Filter filter : filters) {
            if(filter instanceof ApplicationSecurityFilter) {
                securityFilter = (ApplicationSecurityFilter) filter;
                MockMvc mockMvc = standaloneSetup(new CloudtubeCustomerProfileController())
                        .addFilters(securityFilter).build();
                // MockFilterChain filterChain = new MockFilterChain();
                mockMvc.perform(MockMvcRequestBuilders.get("/cloudtube/getCustomerProfile"));
            }
        }

    }
}
