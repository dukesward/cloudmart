package com.web.cloudtube.core.apps.auth.service;

import com.web.cloudtube.core.apps.auth.repository.AppSecurityKeysRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSecurityService {
    static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityService.class);
    private final AppSecurityKeysRepository appSecurityKeysRepository;

    @Autowired
    public ApplicationSecurityService(
            AppSecurityKeysRepository appSecurityKeysRepository
    ) {
        this.appSecurityKeysRepository = appSecurityKeysRepository;
    }


}
