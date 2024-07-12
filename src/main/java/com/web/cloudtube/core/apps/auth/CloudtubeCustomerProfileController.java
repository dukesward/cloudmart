package com.web.cloudtube.core.apps.auth;

import com.web.cloudtube.core.apps.BaseProfile;
import com.web.cloudtube.core.apps.CloudtubeAppsController;
import com.web.cloudtube.core.apps.UserBaseProfile;
import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import com.web.cloudtube.core.apps.auth.service.CustomerLoginService;
import com.web.cloudtube.core.apps.auth.service.CustomerProfileService;
import com.web.cloudtube.core.validation.ValidationResult;
import com.web.cloudtube.core.validation.aspect.RequestValidated;
import com.web.cloudtube.core.validation.service.ApplicationObjectValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CloudtubeCustomerProfileController extends CloudtubeAppsController {
    static final Logger logger = LoggerFactory.getLogger(CloudtubeCustomerProfileController.class);
    private CustomerProfileService custProfileService;
    private CustomerLoginService customerLoginService;

    public CloudtubeCustomerProfileController() {
        this.policySupplier = new ApplicationAuthPolicySupplier();
    }

    @Autowired
    public void setCustomerProfileService(
            CustomerProfileService custProfileService, CustomerLoginService customerLoginService) {
        this.custProfileService = custProfileService;
        this.customerLoginService = customerLoginService;
    }

    @GetMapping("/cloudtube/getCustomerProfile")
    public Map<String, Object> getCustomerProfile(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        String sessionId = request.getRequestedSessionId();
        UserBaseProfile baseProfile = (UserBaseProfile) BaseProfile.getUserProfile(sessionId);
        if(baseProfile != null) {
            CustomerSessionData sessionData = (CustomerSessionData) baseProfile.getProperty("customerSessionData");
            if(sessionData != null) {
                res.put("customer_type", sessionData.getUserType());
                res.put("auth_id", sessionData.getAuthIdId());
                res.put("customer_name", sessionData.getUserId());
            }
        }
        return res;
    }

    //@RequestValidated
    @PostMapping("/cloudtube/processLogin")
    public ResponseEntity<String> processUserLogin(
            @RequestBody Map<String, Object> payload
    ) {
        // Map<String, Object> res = new HashMap<>();
        ApplicationObjectValidationService<Map<String, Object>> validationService
                = new ApplicationAuthRequestValidatorSupplier();
        ValidationResult result = validationService.validate("processEmailLogin", payload);
        if(result == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error validating payload");
        }else if(!result.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unacceptable payload");
        }
        return new ResponseEntity<>(
                CloudtubeAppsController.HTTP_RESPONSE_OK,
                HttpStatus.OK);
    }

    //@RequestValidated
    @PostMapping("/cloudtube/processRegister")
    public ResponseEntity<String> processUserRegister(
            HttpServletRequest request,
            @RequestBody Map<String, Object> payload
    ) {
        String sessionId = request.getRequestedSessionId();
        BaseProfile baseProfile = BaseProfile.getUserProfile(sessionId);
        ApplicationObjectValidationService<Map<String, Object>> validationService
                = new ApplicationAuthRequestValidatorSupplier();
        ValidationResult result = validationService.validate("processEmailRegister", payload);
        if(result == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error validating payload");
        }else if(!result.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unacceptable payload");
        }else {
            if(!this.customerLoginService.updateUserLogin(
                    baseProfile, (String) payload.get("email"), (String) payload.get("password"))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid registration data");
            }
            this.custProfileService.updateUserLogin(baseProfile);
        }
        return new ResponseEntity<>(
                CloudtubeAppsController.HTTP_RESPONSE_OK,
                HttpStatus.OK);
    }
}
