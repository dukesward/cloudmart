package com.web.cloudtube.core.backend;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationDataSourceConfig {

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public HikariDataSource masterDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public HikariDataSource slaveDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "contentDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.content")
    public HikariDataSource contentDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "dynamicDataSource")
    public ApplicationDynamicDataSource dynamicDataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("slaveDataSource") DataSource slaveDataSource,
            @Qualifier("contentDataSource") DataSource contentDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(ApplicationDynamicDataSource.DataSourceType.MASTER.getType(), masterDataSource);
        targetDataSources.put(ApplicationDynamicDataSource.DataSourceType.SLAVE_1.getType(), slaveDataSource);
        targetDataSources.put(ApplicationDynamicDataSource.DataSourceType.CONTENT.getType(), contentDataSource);
        return new ApplicationDynamicDataSource(masterDataSource, targetDataSources);
    }

    @Primary
    @Bean
    public DataSource dataSource(@Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        return new LazyConnectionDataSourceProxy(dynamicDataSource);
    }
}
