package comarch.client.ishift.pl.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.model.BankAccount;
import comarch.client.ishift.pl.model.CompanyData;
import comarch.client.ishift.pl.model.TransferObject;
import comarch.client.ishift.pl.repository.BankAccountRepository;
import comarch.client.ishift.pl.repository.CompanyDataRepository;
import comarch.client.ishift.pl.services.HttpRequestService;
import comarch.client.ishift.pl.services.TransferObjectService;
import comarch.client.ishift.pl.services.XmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TransferObjectServiceImpl implements TransferObjectService {

    private final CompanyDataRepository companyDataRepository;
    private final BankAccountRepository bankAccountRepository;
    private final HttpRequestService httpRequestService;
    private final XmlService xmlService;

    @Autowired
    public TransferObjectServiceImpl(CompanyDataRepository companyDataRepository,
                                     BankAccountRepository bankAccountRepository,
                                     HttpRequestService httpRequestService,
                                     XmlService xmlService) {
        this.companyDataRepository = companyDataRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.httpRequestService = httpRequestService;
        this.xmlService = xmlService;
    }


    @Override
    public String sendTransferObject(AccountingOfficeData accountingOfficeData, String dbName) {
        String companyName = companyDataRepository.getCompanyName().getCompanyData().trim();

        if (xmlService.isNewUserInComarchDb(
                accountingOfficeData,
                companyName)) {

            System.out.println("Tworzę użytkownika");
            CompanyData companyREGON = companyDataRepository.getCompanyREGON();
            List<CompanyData> companyData = companyDataRepository.getCompanyData();

            companyData.add(companyDataRepository.getCompanyIndividualTaxAccount());

            List<BankAccount> bankAccounts = bankAccountRepository.getAllBankAccountsList();

            TransferObject transferObject = new TransferObject(
                    dbName.toLowerCase(),
                    companyName,
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

                xmlService.addNewUser(accountingOfficeData,
                        companyREGON.getCompanyData(),
                        newUserPassword,
                        companyName);


            } catch (IOException e) {
                //TODO
                ClientDatabaseContextHolder.clear();
                e.printStackTrace();
            }
        }
        return companyName;
    }
}
