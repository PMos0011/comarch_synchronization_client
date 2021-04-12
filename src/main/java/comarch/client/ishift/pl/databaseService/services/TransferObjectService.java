package comarch.client.ishift.pl.databaseService.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;

import java.io.IOException;
import java.util.List;

public interface TransferObjectService {

    String sendTransferObject(AccountingOfficeData accountingOfficeData, UserData userData) throws IOException;
}
