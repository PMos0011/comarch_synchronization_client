package comarch.client.ishift.pl.databaseService.bootstrap;

import comarch.client.ishift.pl.dataModel.repository.CompanyDataRepository;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import comarch.client.ishift.pl.databaseService.services.*;
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
    private final UserDataService userDataService;
    private final CompanyDataRepository companyDataRepository;

    @Autowired
    public Bootstrap(HttpRequestService httpRequestService,
                     XmlService xmlService,
                     TransferObjectService transferObjectService,
                     DeclarationDataService declarationDataService,
                     UserDataService userDataService,
                     CompanyDataRepository companyDataRepository) {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null, null);
        this.httpRequestService = httpRequestService;
        this.xmlService = xmlService;
        this.transferObjectService = transferObjectService;
        this.declarationDataService = declarationDataService;
        this.userDataService = userDataService;
        this.companyDataRepository = companyDataRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        AccountingOfficeData accountingOfficeData = xmlService.getAccountingOfficeData();
        System.out.println(accountingOfficeData.getAccountingOfficeName());


        System.out.println("Sprawdzam dostępne bazy");
        for (String dbName : dataBasesListSingleton.getDatabasesList()) {
            ClientDatabaseContextHolder.set(dbName);

            if (xmlService.isNewUserInComarchDb(accountingOfficeData, dbName)) {
                System.out.println("Tworzę użytkownika");
                String companyName = companyDataRepository.getCompanyName().getCompanyData().trim();
                UserData userData = userDataService.createNewUserData(companyName, dbName);
                xmlService.addNewUser(accountingOfficeData, userData);
            }
            ClientDatabaseContextHolder.clear();

        }

        System.out.println("Rozpoczynam synchronizację");
        accountingOfficeData.getUserDataList().forEach(user -> {
            System.out.println(user.getCompanyName());
            if (user.getSynchro()){
                ClientDatabaseContextHolder.set(user.getDbName());
                if(!user.getSuccessfullySynchro())
                    transferObjectService.sendTransferObject(accountingOfficeData, user);

                declarationDataService.sendDeclarationData(accountingOfficeData, user);

                ClientDatabaseContextHolder.clear();
            }
        });


        httpRequestService.sendRequest("/synchro");

        System.out.println("Koniec");

    }
}
