package com.web.cloudtube.core.backend;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

public class ApplicationDynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<DataSourceType> CONTEXT_HOLDER
            = ThreadLocal.withInitial(() -> DataSourceType.MASTER);
    public ApplicationDynamicDataSource(DataSource defaultDataSource, Map<Object, Object> dataSources) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(dataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return CONTEXT_HOLDER.get();
    }

    public static void setDataSourceLookupKey(DataSourceType dataSourceType) {
        CONTEXT_HOLDER.set(dataSourceType);
    }

    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }

    public enum DataSourceType {
        MASTER("master"),
        CONTENT("content"),
        SLAVE_1("slave"),
        SLAVE_2("slave2");

        private final String type;

        DataSourceType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
