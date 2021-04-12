package comarch.client.ishift.pl.databaseService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;

import java.io.IOException;

public interface DeclarationDataService {

    void sendDeclarationData(AccountingOfficeData accountingOfficeData, UserData userData) throws IOException;
}
