package comarch.client.ishift.pl.databaseService.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;

public interface TransferObjectService {

    String sendTransferObject(AccountingOfficeData accountingOfficeData, UserData userData);
}
