package comarch.client.ishift.pl.databaseService.bootstrap;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import comarch.client.ishift.pl.databaseService.services.HttpRequestService;
import comarch.client.ishift.pl.databaseService.services.DeclarationDataService;
import comarch.client.ishift.pl.databaseService.services.TransferObjectService;
import comarch.client.ishift.pl.databaseService.services.XmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


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

        for (String dbName : dataBasesListSingleton.getDatabasesList()) {
            System.out.println(dbName);
            ClientDatabaseContextHolder.set(dbName);

            String companyName = transferObjectService.sendTransferObject(accountingOfficeData, dbName);
            declarationDataService.sendDeclarationData(accountingOfficeData, companyName, dbName);

            ClientDatabaseContextHolder.clear();
        }

        httpRequestService.sendRequest("/synchro");

        System.out.println("Koniec");

    }
}
