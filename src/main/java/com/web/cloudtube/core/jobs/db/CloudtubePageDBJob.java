package com.web.cloudtube.core.apps.jobs.db;

import com.zaxxer.hikari.HikariDataSource;

public class CloudtubePageDBJob {

    private static final DBJobRunner runner = new DBJobRunner(HikariDataSource.class);

    public static void main(String[] args) {

        // runner.runSqlTaskFromFile("/db_dump/application_layouts.sql");
        runner.runSqlTaskFromFile("/db_dump/application_widgets_dashboard.sql");
    }
}
