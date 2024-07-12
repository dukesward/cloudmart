package com.web.cloudtube.core.apps.auth.repository;

import com.web.cloudtube.core.apps.auth.entity.AppSecuredKeys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSecurityKeysRepository extends JpaRepository<AppSecuredKeys, Long> {

}
