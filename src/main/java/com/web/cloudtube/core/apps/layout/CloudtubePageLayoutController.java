package com.web.cloudtube.core.apps.layout;

import com.web.cloudtube.core.apps.BaseProfile;
import com.web.cloudtube.core.apps.CloudtubeAppsController;
import com.web.cloudtube.core.apps.UserBaseProfile;
import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import com.web.cloudtube.core.apps.layout.service.PageContentService;
import com.web.cloudtube.core.apps.layout.service.PageLayoutService;
import com.web.cloudtube.core.apps.preference.service.UserPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cloudtube")
public class CloudtubePageLayoutController extends CloudtubeAppsController {
    static final Logger logger = LoggerFactory.getLogger(CloudtubePageLayoutController.class);
    private UserPreferenceService userPrefService;
    private PageLayoutService pageLayoutService;
    private PageContentService pageContentService;

    public CloudtubePageLayoutController() {
        this.policySupplier = new PageLayoutPolicySupplier();
    }

    @Autowired
    public void setPreferenceService(UserPreferenceService userPrefService) {
        this.userPrefService = userPrefService;
    }

    @Autowired
    public void setPageLayoutService(PageLayoutService pageLayoutService) {
        this.pageLayoutService = pageLayoutService;
    }

    @Autowired
    public void setPageContentService(PageContentService pageContentService) {
        this.pageContentService = pageContentService;
    }

    @GetMapping("/metadata/{page_name}")
    public Map<String, Object> getPageMetadata(
            @PathVariable(name = "page_name") String pageName,
            @RequestParam Optional<String> appId,
            HttpServletRequest request
    ) {
        logger.debug("Enter CloudtubeMetadataController:getPageMetadata, page_name=" + pageName);
        String sessionId = request.getRequestedSessionId();
        UserBaseProfile baseProfile = (UserBaseProfile) BaseProfile.getUserProfile(sessionId);
        CustomerSessionData sessionData = (CustomerSessionData) baseProfile.getProperty("customerProfile");
        Map<String, Object> res =
                this.pageLayoutService.getAssembledLayout(appId.orElseGet(() -> "dashboard"), pageName, sessionData.getUserType());
        logger.debug("Read assembled response from service: " + res.get("layout"));
        return res;
    }

    @GetMapping("/content/{page_name}")
    public Map<String, Object> getPageContent(
            @PathVariable(name = "page_name") String pageName,
            @RequestParam Optional<String> appId
    ) {
        logger.debug("Enter CloudtubeMetadataController:getPageContent, page_name=" + pageName);
        Map<String, Object> res =
                this.pageContentService.getContentsByPage(appId.orElseGet(() -> "dashboard"), pageName);
        return res;
    }
}
