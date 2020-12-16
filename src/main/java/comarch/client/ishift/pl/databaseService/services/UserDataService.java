package comarch.client.ishift.pl.databaseService.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;

import java.io.IOException;

public interface UserDataService {

    UserData createNewUserData(String companyName, String dbName) throws IOException;
}
