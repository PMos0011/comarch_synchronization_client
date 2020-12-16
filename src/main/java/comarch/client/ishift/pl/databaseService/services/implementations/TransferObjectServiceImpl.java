package comarch.client.ishift.pl.databaseService.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.dataModel.model.*;
import comarch.client.ishift.pl.dataModel.repository.ContractorRepository;
import comarch.client.ishift.pl.dataModel.repository.InvoiceRepository;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.databaseService.services.HttpRequestService;
import comarch.client.ishift.pl.dataModel.repository.BankAccountRepository;
import comarch.client.ishift.pl.dataModel.repository.CompanyDataRepository;
import comarch.client.ishift.pl.databaseService.services.JwtService;
import comarch.client.ishift.pl.databaseService.services.TransferObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TransferObjectServiceImpl implements TransferObjectService {

    private final CompanyDataRepository companyDataRepository;
    private final BankAccountRepository bankAccountRepository;
    private final HttpRequestService httpRequestService;
    private final InvoiceRepository invoiceRepository;
    private final ContractorRepository contractorRepository;

    @Autowired
    public TransferObjectServiceImpl(CompanyDataRepository companyDataRepository,
                                     BankAccountRepository bankAccountRepository,
                                     HttpRequestService httpRequestService,
                                     InvoiceRepository invoiceRepository,
                                     ContractorRepository contractorRepository) {
        this.companyDataRepository = companyDataRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.httpRequestService = httpRequestService;
        this.invoiceRepository = invoiceRepository;
        this.contractorRepository = contractorRepository;
    }


    @Override
    public String sendTransferObject(AccountingOfficeData accountingOfficeData, UserData userData) {

        //todo  null exception
        String token = httpRequestService.getAuthorization(accountingOfficeData.getUser(), accountingOfficeData.getPassword());
        String dbId = JwtService.getDbIDFromToken(token);

            CompanyData companyNIP = companyDataRepository.getCompanyNIP();
            List<CompanyData> companyData = companyDataRepository.getCompanyData();

            companyData.add(companyDataRepository.getCompanyIndividualTaxAccount());

            List<BankAccount> bankAccounts = bankAccountRepository.getAllBankAccountsList();

            List<Contractor> contractorList = contractorRepository.findAllById(
                    invoiceRepository.getAllBuyingContractorsIdList()
            );

            List<Invoice> invoiceList = invoiceRepository.getAllSellingInvoices();
            String serverDBName = createDbName(userData.getDbName(), dbId);

            TransferObject transferObject = new TransferObject(
                    serverDBName,
                    userData.getCompanyName(),
                    accountingOfficeData.getAccountingOfficeName(),
                    accountingOfficeData.getUser(),
                    companyNIP.getCompanyData(),
                    companyData,
                    bankAccounts,
                    contractorList,
                    invoiceList);

            try {
                String newUserPassword = httpRequestService.sendRequest(
                        new ObjectMapper().writeValueAsString(transferObject),
                        "/synchro/transferObject",
                        accountingOfficeData.getUser(),
                        accountingOfficeData.getPassword());

                userData.setLogin(companyNIP.getCompanyData());
                userData.setPassword(newUserPassword);
                userData.setServerDBName(serverDBName);
                userData.setSuccessfullySynchro(true);
                userData.setUpdateDate(0L);


            } catch (IOException e) {
                //TODO
                ClientDatabaseContextHolder.clear();
                e.printStackTrace();
            }

        return "companyName";
    }

    private String createDbName(String dbName, String dbId) {

        dbName = dbName.toLowerCase();

        int trim = 63 - (dbName.length() + dbId.length());

        if (trim < 0)
            dbName = dbName.substring(0, dbName.length() + trim);

        return dbName + "_" + dbId;

    }
}
