package comarch.client.ishift.pl.databaseService.configurations;

import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RoutingConfiguration {

    @Bean
    public DataSource getDataSource()  {

        String localHostName = null;
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DataBasesListSingleton dataBasesListSingleton = DataBasesListSingleton.getInstance(localHostName);

        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource client0Datasource = clientADatasource(localHostName,"");

        String finalLocalHostName = localHostName;
        dataBasesListSingleton.getDatabasesList().forEach(name -> {
            DataSource clientADatasource = clientADatasource(finalLocalHostName, name);
            targetDataSources.put(name,
                    clientADatasource);
        });

        ClientDataSourceRouter clientRoutingDatasource
                = new ClientDataSourceRouter();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(client0Datasource);
        return clientRoutingDatasource;
    }

    private DataSource clientADatasource(String host, String databaseName) {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url("jdbc:sqlserver://" + host + "\\OPTIMA;databaseName=" + databaseName);
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("Comarch!2011");
        return dataSourceBuilder.build();
    }
}
