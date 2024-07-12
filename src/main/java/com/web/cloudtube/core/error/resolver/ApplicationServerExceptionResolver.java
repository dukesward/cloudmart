package com.web.cloudtube.core.error.resolver;

import com.web.cloudtube.core.error.HttpErrorResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApplicationServerExceptionResolver implements HandlerExceptionResolver, Ordered {
    static final Logger logger = LoggerFactory.getLogger(ApplicationServerExceptionResolver.class);

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if(handler instanceof HttpErrorResponseWrapper) {
            return this.doResolveException(request, response,
                    (HttpErrorResponseWrapper) handler, ex);
        }else {
            return null;
        }
    }

    private ModelAndView doResolveException(
            HttpServletRequest request, HttpServletResponse response,
            HttpErrorResponseWrapper responseWrapper, Exception ex) {
        logger.error("Exception caught for: " + ex.getMessage());
        ModelAndView model = new ModelAndView();
        model.setView(new MappingJackson2JsonView());
        model.addObject("timestamp", responseWrapper.getTimestamp().getTime());
        model.addObject("error", responseWrapper.getHttpErrorType());
        model.addObject("message", responseWrapper.getMessage());
        model.setStatus(responseWrapper.getHttpErrorType());
        return model;
    }
}
