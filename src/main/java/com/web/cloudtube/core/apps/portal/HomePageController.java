package com.web.cloudtube.core.apps.portal;

import com.web.cloudtube.core.apps.CloudtubeAppsController;
import com.web.cloudtube.core.apps.preference.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomePageController extends CloudtubeAppsController {
    private UserPreferenceService userPrefService;

    public HomePageController(UserPreferenceService userPrefService) {
        this.policySupplier = new PortalPolicySupplier();
    }

    @Autowired
    public void setPreferenceService(UserPreferenceService userPrefService) {
        this.userPrefService = userPrefService;
    }

    // http://127.0.0.1:8080/home
    @GetMapping("/cloudtube/dsd/welcome")
    public ResponseEntity<Map<String, String>> welcomeMessage() {
        Map<String, String> res = new HashMap<>();
        res.put("message", "welcome");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
