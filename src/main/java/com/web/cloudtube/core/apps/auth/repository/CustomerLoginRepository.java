package com.web.cloudtube.core.apps.auth.repository;

import com.web.cloudtube.core.apps.auth.entity.CustomerLoginData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerLoginRepository extends JpaRepository<CustomerLoginData, Long> {

    public List<CustomerLoginData> getByAccessId(String accessId);

    public CustomerLoginData findFirstByUserIdOrderByIdDesc(String userId);

    public List<CustomerLoginData> findByAccessIdAndUserId(String accessId, String userId);
}
