package comarch.client.ishift.pl.services;

import comarch.client.ishift.pl.accountingOfficeSettings.AccountingOfficeData;

public interface TransferObjectService {

    String sendTransferObject(AccountingOfficeData accountingOfficeData, String dbName);
}
