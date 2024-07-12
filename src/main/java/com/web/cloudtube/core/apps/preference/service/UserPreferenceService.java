package com.web.cloudtube.core.apps.preference.service;

import com.web.cloudtube.core.apps.preference.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {
    private final UserPreferenceRepository repository;

    @Autowired
    public UserPreferenceService(UserPreferenceRepository repository) {
        this.repository = repository;
    }
}
