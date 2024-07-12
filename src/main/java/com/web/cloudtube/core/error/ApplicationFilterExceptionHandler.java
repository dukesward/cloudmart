package com.web.cloudtube.core.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationFilterExceptionHandler extends OncePerRequestFilter {
    static final Logger logger = LoggerFactory.getLogger(ApplicationFilterExceptionHandler.class);

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }catch (ResponseStatusException e) {
            logger.error("Error processing filters with http status: " + e.getStatus());
            int httpStatusCode = e.getRawStatusCode();
            HttpErrorResponseWrapper responseWrapper = new HttpErrorResponseWrapper(httpStatusCode, e.getReason());
            //this.resolver.resolveException(request, response, responseWrapper, e);
            request.setAttribute("wrappedResponse", responseWrapper);
            if(request.getAttribute("policyRedirect") != null) {
                String policyRedirect = String.valueOf(request.getAttribute("policyRedirect"));
                logger.debug("Policy rejected and redirect to " + policyRedirect);
                request.getRequestDispatcher(policyRedirect).forward(request, response);
            }else {
                logger.error("Error processing filters with status: " + e.getStatus());
                response.setStatus(httpStatusCode);
                response.setContentType("application/json");
                request.getRequestDispatcher("/error").forward(request, response);
            }
        }catch (Exception e) {
            logger.error("Error processing filters and redirect to /error: " + e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            HttpErrorResponseWrapper responseWrapper = new HttpErrorResponseWrapper(status.value(), e.getMessage());
            request.setAttribute("wrappedResponse", responseWrapper);
            response.setStatus(status.value());
            response.setContentType("application/json");
            request.getRequestDispatcher("/error").forward(request, response);
        }
    }
}
