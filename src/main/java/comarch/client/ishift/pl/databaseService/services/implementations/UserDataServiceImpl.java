package comarch.client.ishift.pl.databaseService.services.implementations;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.services.UserDataService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class UserDataServiceImpl implements UserDataService {

    @Override
    public UserData createNewUserData(String companyName, String dbName) throws IOException {

        //System.out.println("synchronizować dane klienta " + companyName + " z bazy: " + dbName +   "?");
        return new UserData(companyName, dbName,false, false);
    }
}
