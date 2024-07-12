package com.web.cloudtube.core.security;

import com.web.cloudtube.core.apps.CloudtubeAppsController;
import com.web.cloudtube.core.apps.auth.service.CustomerLoginService;
import com.web.cloudtube.core.error.ApplicationFilterExceptionHandler;
import com.web.cloudtube.core.security.auth.ApplicationBasicPolicy;
import com.web.cloudtube.core.security.auth.ApplicationPolicyRuleFilter;
import com.web.cloudtube.core.security.auth.ApplicationSecurityFilter;
import com.web.cloudtube.core.apps.auth.service.CustomerProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    static final Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    @Autowired
    private ListableBeanFactory beanFactory;

    @Autowired
    private CustomerProfileService customerProfileService;

    @Autowired
    private CustomerLoginService customerLoginService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationFilterExceptionHandler exceptionHandler;

    @Bean("customSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Customized authorization logic goes here");
        // define session management policy
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        // add custom security filter to filter chain
        http
                .addFilterBefore(
                new ApplicationSecurityFilter(
                        this.applySecurityPolicy(http), this.customerProfileService),
                FilterSecurityInterceptor.class
                )
                .addFilterAfter(
                new ApplicationPolicyRuleFilter(this.context), ApplicationSecurityFilter.class
                )
                .addFilterBefore(this.exceptionHandler, ApplicationSecurityFilter.class)
                .requiresChannel(channel ->
                channel.anyRequest().requiresSecure());
        // actual authorize logic will be put in ApplicationSecurityFilter
        http.anonymous().principal("anonymous");
        http.authorizeRequests((requests) -> requests.anyRequest().permitAll());
        // configures all requests to be protected by csrf token
        http.csrf(csrf -> csrf.csrfTokenRepository
                (getCsrfTokenRepository()));
        http.cors(cors -> cors.configurationSource(apiConfigurationSource()));
        // http.httpBasic();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http, PasswordEncoder passwordEncoder, CustomerLoginService customerLoginService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.eraseCredentials(false)
                .userDetailsService(customerLoginService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        String[] allowedHeaders = {
                "authorization", "content-type", "X-XSRF-TOKEN"};
        configuration.setAllowedOrigins(List.of("https://192.168.0.101:4200", "https://192.168.0.100:4200"));
        configuration.setAllowedHeaders(Arrays.asList(allowedHeaders));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","OPTIONS"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private List<ApplicationBasicPolicy> applySecurityPolicy(HttpSecurity http) {
        List<ApplicationBasicPolicy> policies = new ArrayList<>();
        try {
            Map<String, Object> controllers
                    = beanFactory.getBeansWithAnnotation(RestController.class);
            for(var entry: controllers.entrySet()) {
                Object controller = entry.getValue();
                if(controller instanceof CloudtubeAppsController) {
                    CloudtubeAppsController appsController = (CloudtubeAppsController) controller;
                    policies.addAll(appsController.getPolicies());
                }
            }
        }catch (Exception e) {
            logger.error("Exception during application security policy detection", e);
        }
        return policies;
    }

    private CookieCsrfTokenRepository getCsrfTokenRepository() {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setCookiePath("/");
        return tokenRepository;
    }
}
