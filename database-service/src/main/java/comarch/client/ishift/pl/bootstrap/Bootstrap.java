package comarch.client.ishift.pl.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.data.DataBasesListSingleton;
import comarch.client.ishift.pl.model.*;
import comarch.client.ishift.pl.repository.BankAccountRepository;
import comarch.client.ishift.pl.repository.CompanyDataRepository;
import comarch.client.ishift.pl.repository.DeclarationDataRepository;
import comarch.client.ishift.pl.services.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static comarch.client.ishift.pl.services.XmlService.*;


@Component
public class Bootstrap implements CommandLineRunner {

    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final HttpRequestService httpRequestService;
    private final CompanyDataRepository companyDataRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public Bootstrap(DeclarationDataRepository declarationDataRepository,
                     HttpRequestService httpRequestService,
                     CompanyDataRepository companyDataRepository,
                     BankAccountRepository bankAccountRepository) {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null);
        this.declarationDataRepository = declarationDataRepository;
        this.httpRequestService = httpRequestService;
        this.companyDataRepository = companyDataRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        AccountingOfficeData accountingOfficeData = readAccountingOfficeSettings();

        if (accountingOfficeData.getPassword() == null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("podaj hasło");
            accountingOfficeData.setPassword(reader.readLine());
        }

        Optional<List<UserData>> optionalUserDataList = Optional.ofNullable(accountingOfficeData.getUserDataList());
        accountingOfficeData.setUserDataList(
                optionalUserDataList.orElse(new ArrayList<>())
        );

        for (String dbName : dataBasesListSingleton.getDatabasesList()) {

            ClientDatabaseContextHolder.set(dbName);
            CompanyData companyName = companyDataRepository.getCompanyName();


            System.out.println(companyName.getCompanyData());

            if (accountingOfficeData.getUserDataList().stream()
                    .noneMatch(user -> user.getCompanyName().equals(companyName.getCompanyData()))) {

                System.out.println("Tworzę użytkownika");
                CompanyData companyREGON = companyDataRepository.getCompanyREGON();
                List<CompanyData> companyData = companyDataRepository.getCompanyData();

                companyData.add(companyDataRepository.getCompanyIndividualTaxAccount());

                List<BankAccount> bankAccounts = bankAccountRepository.getAllBankAccountsList();

                TransferObject transferObject = new TransferObject(
                        dbName.toLowerCase(),
                        companyName.getCompanyData(),
                        accountingOfficeData.getAccountingOfficeName(),
                        accountingOfficeData.getUser(),
                        companyREGON.getCompanyData(),
                        companyData,
                        bankAccounts);

                try {
                    String newUserPassword = httpRequestService.sendRequest(
                            new ObjectMapper().writeValueAsString(transferObject),
                            "/synchro/transferObject",
                            accountingOfficeData.getUser(),
                            accountingOfficeData.getPassword());

                    accountingOfficeData.getUserDataList().add(new UserData(
                            companyREGON.getCompanyData(),
                            newUserPassword,
                            companyName.getCompanyData(),
                            0L));

                    writeAccountingOfficeSettings(accountingOfficeData);

                } catch (IOException e) {
                    //TODO
                    ClientDatabaseContextHolder.clear();
                    e.printStackTrace();
                }
            }

            try {
                UserData userData = accountingOfficeData.getUserDataList().stream()
                        .filter(user -> user.getCompanyName().equals(companyName.getCompanyData()))
                        .findFirst()
                        .orElseThrow(()->new RuntimeException("brak użytkownika"));

                Long timestamp = new Timestamp(System.currentTimeMillis()).getTime();

                httpRequestService.sendRequest(
                        new ObjectMapper().writeValueAsString(getData(userData.getUpdateDate())),
                        "/synchro/documents/" + dbName.toLowerCase(),
                        accountingOfficeData.getUser(),
                        accountingOfficeData.getPassword());

                userData.setUpdateDate(timestamp);

                 writeAccountingOfficeSettings(accountingOfficeData);


            } catch (IOException e) {
                System.out.println("bład konwersji");
                ClientDatabaseContextHolder.clear();
            } catch (RuntimeException e) {
                System.out.println(e.toString());
            }

            ClientDatabaseContextHolder.clear();
        }

         httpRequestService.sendRequest("/synchro");

    }


    public List<DeclarationData> getData(Long date) {

        System.out.println(new Date(date));
        System.out.println("Sprawdzam dokumenty");

        Optional<List<DeclarationData>> declarationDataOptional = declarationDataRepository.findAllSupportedDeclarations(new Date(date));

        List<DeclarationData> declarationData = declarationDataOptional.orElseThrow(() -> new RuntimeException("brak dokumentów"));
        System.out.println("zanlezione: " + declarationData.size());

        for (DeclarationData decl : declarationData) {
            List<DeclarationDetails> declarationDetails =
                    decl.getDeclarationDetails().stream()
                            .filter(this::detailFilter)
                            .filter(d -> d.getValue().length() > 3)
                            .filter(this::hasDotFilter)
                            .filter(d -> !d.getDescription().contains("Rodzaj płatnika"))
                            .collect(Collectors.toList());
            decl.setDeclarationDetails(declarationDetails);
        }
        return declarationData;
    }

    public boolean detailFilter(DeclarationDetails d) {

        try {
            String tmp = d.getValue().replace(",", "");
            double val = Double.parseDouble(tmp);

            return val >= 0.0;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasDotFilter(DeclarationDetails d) {

        try {
            Character a = d.getValue().charAt(d.getValue().length() - 3);
            return a.equals('.');

        } catch (Exception e) {
            return false;
        }
    }


    private void writeNewClientAccessData(String login, String password, String fileName) throws IOException {
        String accessData = "login: " + login +
                " hasło: " + password;
        fileName = System.getProperty("user.dir") + "/" + fileName + ".txt";

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(accessData);
        writer.close();

        System.out.println(accessData);
    }

}
