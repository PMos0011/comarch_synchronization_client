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

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean synchro = false;

        System.out.println("synchronizować dane klienta " + companyName + "?");
        System.out.println("t/n");
        int answer = reader.read();

        if(answer==116)
            synchro=true;

        return new UserData(companyName, dbName,synchro, false);
    }
}
