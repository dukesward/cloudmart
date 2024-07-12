package com.web.cloudtube.core.error;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class HttpErrorResponseWrapper {
    private int httpErrorStatus;
    private String message;
    private HttpStatus httpErrorType;
    private Date timestamp;

    public HttpErrorResponseWrapper(int httpErrorStatus, String message) {
        this.httpErrorStatus = httpErrorStatus;
        this.httpErrorType = HttpStatus.resolve(httpErrorStatus);
        this.message = message;
        this.timestamp = new Date();
    }

    public int getHttpErrorStatus() {
        return httpErrorStatus;
    }

    public void setHttpErrorStatus(int httpErrorStatus) {
        this.httpErrorStatus = httpErrorStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpErrorType() {
        return httpErrorType;
    }

    public void setHttpErrorType(HttpStatus httpErrorType) {
        this.httpErrorType = httpErrorType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
