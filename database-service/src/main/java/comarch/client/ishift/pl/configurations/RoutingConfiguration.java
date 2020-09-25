package comarch.client.ishift.pl.configurations;

import comarch.client.ishift.pl.data.DataBasesListSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RoutingConfiguration {

    private final String[] args;
    private final DataBasesListSingleton dataBasesListSingleton;

    @Autowired
    public RoutingConfiguration(ApplicationArguments applicationArguments) throws SQLException {
        this.args = applicationArguments.getSourceArgs();
        dataBasesListSingleton = DataBasesListSingleton.getInstance(args[0]);
    }

    @Bean
    public DataSource getDataSource()  {

        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource client0Datasource = clientADatasource("");

        dataBasesListSingleton.getDatabasesList().forEach(name -> {
            DataSource clientADatasource = clientADatasource(name);
            targetDataSources.put(name,
                    clientADatasource);
        });

        ClientDataSourceRouter clientRoutingDatasource
                = new ClientDataSourceRouter();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(client0Datasource);
        return clientRoutingDatasource;
    }

    private DataSource clientADatasource(String databaseName) {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url("jdbc:sqlserver://" + args[0] + "\\OPTIMA;databaseName=" + databaseName);
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("Comarch!2011");
        return dataSourceBuilder.build();
    }
}
