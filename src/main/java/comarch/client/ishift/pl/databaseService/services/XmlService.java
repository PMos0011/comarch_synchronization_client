package comarch.client.ishift.pl.databaseService.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;

import java.io.File;
import java.io.IOException;

public interface XmlService {

    static AccountingOfficeData readAccountingOfficeSettings() throws IOException {

        return new XmlMapper().readValue(
                new File(System.getProperty("user.dir") + "/data.xml"),
                AccountingOfficeData.class);
    }

     static void writeAccountingOfficeSettings(AccountingOfficeData accountingOfficeData) throws IOException {
        new XmlMapper().writeValue(
                new File(System.getProperty("user.dir") + "/data.xml"),
                accountingOfficeData);
    }


    AccountingOfficeData getAccountingOfficeData() throws IOException;

    boolean isNewUserInComarchDb(AccountingOfficeData accountingOfficeData, String companyName);

    UserData getUserData(AccountingOfficeData accountingOfficeData, String companyName);

    void addNewUser(AccountingOfficeData accountingOfficeData, UserData userData) throws IOException;





}
