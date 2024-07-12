package com.web.cloudtube.core.apps.auth.repository;

import com.web.cloudtube.core.apps.auth.entity.CustomerSessionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerSessionRespository extends JpaRepository<CustomerSessionData, Long> {

    public CustomerSessionData getBySessionId(String sessionId);
    public CustomerSessionData getByAuthId(String authId);

    @Modifying
    @Query("update CustomerSessionData c set c.sessionId = :sessionId where c.authId = :authId")
    void updateCustomerSessionDataByAuthId(@Param(value = "authId") String authId, @Param(value = "sessionId") String sessionId);

    @Modifying
    @Query("update CustomerSessionData c set c.userId = :userId, c.userType = :userType where c.id = :id")
    void updateCustomerSessionDataByUserId(
            @Param(value = "userId") String userId,
            @Param(value = "userType") String userType,
            @Param(value = "id") String id);
}
