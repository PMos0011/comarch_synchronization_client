package comarch.client.ishift.pl.services;

import comarch.client.ishift.pl.accountingOfficeSettings.AccountingOfficeData;

public interface DeclarationDataService {

    void sendDeclarationData(AccountingOfficeData accountingOfficeData, String companyName, String dbName);
}
