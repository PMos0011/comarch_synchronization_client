package comarch.client.ishift.pl.databaseService.configurations;

import com.zaxxer.hikari.HikariDataSource;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import comarch.client.ishift.pl.databaseService.services.XmlService;
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

    private HikariDataSource clientADatasource(String host, String databaseName, String password) {

        HikariDataSource dataSourceBuilder = new HikariDataSource();
        dataSourceBuilder.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSourceBuilder.setJdbcUrl("jdbc:sqlserver://" + host + "\\OPTIMA;databaseName=" + databaseName);
        dataSourceBuilder.setUsername("sa");
        dataSourceBuilder.setPassword(password);
        return dataSourceBuilder;
    }
}
