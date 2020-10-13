package comarch.client.ishift.pl.databaseService.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.dataModel.model.*;
import comarch.client.ishift.pl.dataModel.repository.ContractorRepository;
import comarch.client.ishift.pl.dataModel.repository.InvoiceRepository;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.databaseService.services.HttpRequestService;
import comarch.client.ishift.pl.dataModel.repository.BankAccountRepository;
import comarch.client.ishift.pl.dataModel.repository.CompanyDataRepository;
import comarch.client.ishift.pl.databaseService.services.TransferObjectService;
import comarch.client.ishift.pl.databaseService.services.XmlService;
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
    private final InvoiceRepository invoiceRepository;
    private final ContractorRepository contractorRepository;

    @Autowired
    public TransferObjectServiceImpl(CompanyDataRepository companyDataRepository,
                                     BankAccountRepository bankAccountRepository,
                                     HttpRequestService httpRequestService,
                                     XmlService xmlService,
                                     InvoiceRepository invoiceRepository,
                                     ContractorRepository contractorRepository) {
        this.companyDataRepository = companyDataRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.httpRequestService = httpRequestService;
        this.xmlService = xmlService;
        this.invoiceRepository = invoiceRepository;
        this.contractorRepository = contractorRepository;
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

            List<Contractor> contractorList = contractorRepository.findAllById(
                    invoiceRepository.getAllBuyingContractorsIdList()
            );

            List<Invoice> invoiceList = invoiceRepository.getAllSellingInvoices();

            TransferObject transferObject = new TransferObject(
                    dbName.toLowerCase(),
                    companyName,
                    accountingOfficeData.getAccountingOfficeName(),
                    accountingOfficeData.getUser(),
                    companyREGON.getCompanyData(),
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
