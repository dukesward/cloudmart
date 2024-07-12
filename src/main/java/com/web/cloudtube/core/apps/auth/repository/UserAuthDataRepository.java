package com.web.cloudtube.core.apps.auth.repository;

import com.web.cloudtube.core.apps.auth.entity.UserAuthProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthDataRepository extends JpaRepository<UserAuthProfile, Long> {

}
