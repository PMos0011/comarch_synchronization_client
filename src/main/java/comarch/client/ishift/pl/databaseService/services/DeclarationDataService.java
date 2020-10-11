package comarch.client.ishift.pl.databaseService.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;

public interface DeclarationDataService {

    void sendDeclarationData(AccountingOfficeData accountingOfficeData, String companyName, String dbName);
}
