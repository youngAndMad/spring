package kz.danekerscode.jpareaderwriterds.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class ClientDataSourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("Current datasource type is {}", DatasourceTypeContextHolder.getDatasourceType());
        return DatasourceTypeContextHolder.getDatasourceType();
    }
}
