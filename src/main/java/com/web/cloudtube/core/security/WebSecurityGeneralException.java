package com.web.cloudtube.core.security;

import org.springframework.core.NestedRuntimeException;

public class WebSecurityGeneralException extends NestedRuntimeException  {
    public WebSecurityGeneralException(String msg) {
        super(msg);
    }
}
