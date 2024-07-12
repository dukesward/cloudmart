package com.web.cloudtube.core.apps.jobs.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;

public class DBJobRunner {
    static final Logger logger = LoggerFactory.getLogger(DBJobRunner.class);
    private final DataSource dataSource;

    public DBJobRunner(Class<? extends DataSource> type) {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setName("defaultDataSource");
        properties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        properties.setUrl("jdbc:mysql://localhost:3306/cloudtube?serverTimezone=UTC");
        properties.setUsername("cloud_admin");
        properties.setPassword("Pa55w0rd");
        this.dataSource = createDataSource(properties, type);
    }

    public void runSqlTaskFromFile(String filepath) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            Resource resource = new ClassPathResource(filepath);
            ScriptUtils.executeSqlScript(connection, resource);
        } catch (SQLException e) {
            logger.error("Error occurred while executing sql for " + filepath);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends DataSource> T createDataSource(
            DataSourceProperties properties,
            Class<? extends DataSource> type
    ) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
}
