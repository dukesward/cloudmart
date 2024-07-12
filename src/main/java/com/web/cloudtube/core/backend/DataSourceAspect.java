package com.web.cloudtube.core.backend;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class DataSourceAspect {
    @Before("@annotation(dataSource)")
    public void dataSourcePoint(JoinPoint joinPoint, DynamicDataSource dataSource) {
        ApplicationDynamicDataSource.setDataSourceLookupKey(dataSource.value());
    }

    @After("@annotation(dataSource)")
    public void restoreDataSource(JoinPoint joinPoint, DynamicDataSource dataSource) {
        ApplicationDynamicDataSource.clearDataSourceType();
    }
}
