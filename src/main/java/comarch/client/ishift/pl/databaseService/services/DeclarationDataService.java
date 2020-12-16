package comarch.client.ishift.pl.databaseService.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;

public interface DeclarationDataService {

    void sendDeclarationData(AccountingOfficeData accountingOfficeData, UserData userData);
}
