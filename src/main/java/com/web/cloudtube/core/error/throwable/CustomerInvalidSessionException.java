package com.web.cloudtube.core.error.throwable;

import com.web.cloudtube.core.error.HttpErrorResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomerInvalidSessionException extends Exception {
    private HttpErrorResponseWrapper errorResponse;

    public CustomerInvalidSessionException() {
        this("Customer session is invalid");
    }

    public CustomerInvalidSessionException(String message) {
        super(message);
        this.errorResponse = new HttpErrorResponseWrapper(500, message);
    }

    public HttpErrorResponseWrapper getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(HttpErrorResponseWrapper errorResponse) {
        this.errorResponse = errorResponse;
    }
}
