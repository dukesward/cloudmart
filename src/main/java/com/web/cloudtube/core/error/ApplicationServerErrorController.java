package com.web.cloudtube.core.error;

import com.web.cloudtube.core.apps.CloudtubeAppsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApplicationServerErrorController extends CloudtubeAppsController implements ErrorController {
    static final Logger logger = LoggerFactory.getLogger(ApplicationServerErrorController.class);

    public ApplicationServerErrorController() { this.policySupplier = new ApplicationErrorPagePolicySupplier(); }

    @RequestMapping (value = "/error", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public Map<String, Object> serverInternalError(HttpErrorResponseWrapper responseWrapper) {
        Map<String, Object> result = new HashMap<>();
        result.put("errorStatus", responseWrapper.getHttpErrorStatus());
        result.put("errorType", responseWrapper.getHttpErrorType());
        result.put("message", responseWrapper.getMessage());
        result.put("timestamp", responseWrapper.getTimestamp());
        return result;
    }
}
