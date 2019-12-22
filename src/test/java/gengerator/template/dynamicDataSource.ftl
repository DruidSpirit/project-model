package ${cfg.packageName}.common.config.DataSourceConfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return  DbContextHolder.getDbType();
    }
}
