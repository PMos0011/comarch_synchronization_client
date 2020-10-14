package comarch.client.ishift.pl.databaseService.bootstrap;

import comarch.client.ishift.pl.dataModel.model.Contractor;
import comarch.client.ishift.pl.dataModel.repository.ContractorRepository;
import comarch.client.ishift.pl.dataModel.repository.InvoiceRepository;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import comarch.client.ishift.pl.databaseService.services.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class Bootstrap implements CommandLineRunner {

    private final DataBasesListSingleton dataBasesListSingleton;
    private final HttpRequestService httpRequestService;
    private final XmlService xmlService;
    private final TransferObjectService transferObjectService;
    private final DeclarationDataService declarationDataService;

    @Autowired
    public Bootstrap(HttpRequestService httpRequestService,
                     XmlService xmlService,
                     TransferObjectService transferObjectService,
                     DeclarationDataService declarationDataService) {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null);
        this.httpRequestService = httpRequestService;
        this.xmlService = xmlService;
        this.transferObjectService = transferObjectService;
        this.declarationDataService = declarationDataService;
    }

    @Override
    public void run(String... args) throws Exception {

        AccountingOfficeData accountingOfficeData = xmlService.getAccountingOfficeData();
        System.out.println(accountingOfficeData.getAccountingOfficeName());
//todo  null exception
        String token = httpRequestService.getAuthorization(accountingOfficeData.getUser(), accountingOfficeData.getPassword());
        String dbId = JwtService.getDbIDFromToken(token);


        for (String dbName : dataBasesListSingleton.getDatabasesList()) {
            System.out.println(dbName);
            ClientDatabaseContextHolder.set(dbName);

            String newDbName = createDbName(dbName,dbId);
            String companyName = transferObjectService.sendTransferObject(accountingOfficeData, newDbName);
            System.out.println(dbName);
            declarationDataService.sendDeclarationData(accountingOfficeData, companyName, newDbName);

            ClientDatabaseContextHolder.clear();
        }

        httpRequestService.sendRequest("/synchro");

        System.out.println("Koniec");

    }

    private String createDbName(String dbName, String dbId) {

        dbName = dbName.toLowerCase();

        int trim = 63 - (dbName.length() + dbId.length());

        if (trim < 0)
            dbName = dbName.substring(0, dbName.length() + trim);

        return dbName + "_" + dbId;

    }
}
