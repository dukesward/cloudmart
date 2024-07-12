package com.web.cloudtube.core.backend;

import java.lang.annotation.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DynamicDataSource {

    ApplicationDynamicDataSource.DataSourceType value() default ApplicationDynamicDataSource.DataSourceType.MASTER;
}
