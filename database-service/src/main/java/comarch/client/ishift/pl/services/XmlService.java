package comarch.client.ishift.pl.services;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import comarch.client.ishift.pl.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.accountingOfficeSettings.UserData;

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

    void addNewUser(AccountingOfficeData accountingOfficeData, String regon, String newPassword, String companyName) throws IOException;





}
