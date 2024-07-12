package com.web.cloudtube.core.apps.auth.service;

import com.web.cloudtube.core.apps.distributed.SnowFlakeIdGenerator;

public class CustomerIdGenerator extends SnowFlakeIdGenerator {

    public CustomerIdGenerator(long workerId, long dataCenterId) {
        super(workerId, dataCenterId);
    }
}
