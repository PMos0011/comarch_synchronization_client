package comarch.client.ishift.pl.databaseService.bootstrap;

import comarch.client.ishift.pl.dataModel.repository.CompanyDataRepository;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.databaseService.data.DataBasesListSingleton;
import comarch.client.ishift.pl.databaseService.services.*;
import comarch.client.ishift.pl.gui.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static comarch.client.ishift.pl.databaseService.bootstrap.SynchroEventList.synchroEventList;

@Component
public class Bootstrap implements CommandLineRunner {

    private static Bootstrap bootstrap;

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
        bootstrap = this;
    }

    public static Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public void run(String... args) throws IOException {
        System.out.println("bootstrap");
        AccountingOfficeData accountingOfficeData = getUsers();
        MainWindow.getMainWindow().setAndDisplayUsers(accountingOfficeData);
    }

    public void makeSynchro(AccountingOfficeData accountingOfficeData) {
        synchroEventList = new ArrayList<>();
        System.out.println("Rozpoczynam synchronizację");
        try {
            accountingOfficeData.getUserDataList().forEach(user -> {

                if (!user.getCompanyName().equals("")) {
                    if (user.getSynchro()) {
                        try {
                            ClientDatabaseContextHolder.set(user.getDbName());
                            if (!user.getSuccessfullySynchro()) {
                                transferObjectService.sendTransferObject(accountingOfficeData, user);
                            }
                            synchroEventList.add(user.getCompanyName());
                            declarationDataService.sendDeclarationData(accountingOfficeData, user);
                            synchroEventList.add("wysłałem");
                            XmlService.writeAccountingOfficeSettings(accountingOfficeData);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null,
                                    "Błąd autoryzacji, przerywam",
                                    "Transfer Info",
                                    JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                            synchroEventList.add(e.toString());
                            MainWindow.getMainWindow().getContentLayout().displaySynchroContent(synchroEventList);
                            Thread.currentThread().interrupt();
                        }catch (RuntimeException e){
                            synchroEventList.add("brak nowych dokumentów");
                        }
                        ClientDatabaseContextHolder.clear();
                        synchroEventList.add("------------------------------------");
                    }
                }
            });
            httpRequestService.sendRequest("/synchro");
            JOptionPane.showMessageDialog(null,
                    "Transfer zakończony",
                    "Transfer Info",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            synchroEventList.add(e.toString());
            e.printStackTrace();
        }
        synchroEventList.add("Koniec");
        synchroEventList.add(" ");
        MainWindow.getMainWindow().getContentLayout().displaySynchroContent(synchroEventList);
    }

    private AccountingOfficeData getUsers() throws IOException {
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
        return accountingOfficeData;
    }
}
