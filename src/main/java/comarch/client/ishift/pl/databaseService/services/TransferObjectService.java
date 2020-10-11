package comarch.client.ishift.pl.databaseService.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;

public interface TransferObjectService {

    String sendTransferObject(AccountingOfficeData accountingOfficeData, String dbName);
}
