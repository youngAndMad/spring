package kz.danekerscode.jpareaderwriterds.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@Slf4j
public class DatasourceConfig {

    @Bean
    DataSource dataSource() {
        var readerDataSource = createEmbeddedDatasource(DatasourceType.READER.name());
        var writerDataSource = createEmbeddedDatasource(DatasourceType.WRITER.name());

        var targetDataSources = new HashMap<>();
        targetDataSources.put(DatasourceType.READER, readerDataSource);
        targetDataSources.put(DatasourceType.WRITER, writerDataSource);

        var clientRoutingDatasource
                = new ClientDataSourceRouter();

        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(readerDataSource);

        return clientRoutingDatasource;
    }

    private DataSource createEmbeddedDatasource(String name) {
        EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
        return dbBuilder.setType(EmbeddedDatabaseType.H2)
                .addScript("customers-init.sql")
                .setName(name)
                .build();
    }
}
