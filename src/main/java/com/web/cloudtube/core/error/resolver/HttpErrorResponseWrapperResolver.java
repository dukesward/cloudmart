package com.web.cloudtube.core.error.resolver;

import com.web.cloudtube.core.error.HttpErrorResponseWrapper;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class HttpErrorResponseWrapperResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(HttpErrorResponseWrapper.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        Object responseWrapper = webRequest.getAttribute("wrappedResponse", 0);
        return responseWrapper instanceof HttpErrorResponseWrapper ? (HttpErrorResponseWrapper) responseWrapper : null;
    }
}
