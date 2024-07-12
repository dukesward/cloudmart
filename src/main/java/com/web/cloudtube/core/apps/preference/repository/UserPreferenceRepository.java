package com.web.cloudtube.core.apps.preference.repository;

import com.web.cloudtube.core.apps.preference.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {

}
