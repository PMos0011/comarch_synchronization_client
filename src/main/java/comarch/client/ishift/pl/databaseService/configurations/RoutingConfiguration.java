package comarch.client.ishift.pl.databaseService.configurations;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import comarch.client.ishift.pl.databaseService.services.XmlService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RoutingConfiguration {

    @Bean
    public DataSource getDataSource() throws IOException {

        //todo passing password from console
        AccountingOfficeData accountingOfficeData = XmlService.readAccountingOfficeSettings();

        DataBasesListSingleton dataBasesListSingleton = DataBasesListSingleton.getInstance(accountingOfficeData.getComarchServerAddress(), accountingOfficeData.getComarchServerPassword());

        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource client0Datasource = clientADatasource(accountingOfficeData.getComarchServerAddress(),"", accountingOfficeData.getComarchServerPassword());

        dataBasesListSingleton.getDatabasesList().forEach(name -> {
            DataSource clientADatasource = clientADatasource(accountingOfficeData.getComarchServerAddress(), name, accountingOfficeData.getComarchServerPassword());
            targetDataSources.put(name,
                    clientADatasource);
        });

        ClientDataSourceRouter clientRoutingDatasource
                = new ClientDataSourceRouter();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(client0Datasource);
        return clientRoutingDatasource;
    }

    private DataSource clientADatasource(String host, String databaseName, String password) {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.url("jdbc:sqlserver://" + host + "\\OPTIMA;databaseName=" + databaseName);
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}
