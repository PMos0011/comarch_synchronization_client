package comarch.client.ishift.pl.databaseService.services.implementations;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.services.XmlService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class XmlServiceImpl implements XmlService {

    @Override
    public void addNewUser(AccountingOfficeData accountingOfficeData, UserData userData) throws IOException {

        accountingOfficeData.getUserDataList().add(userData);

        XmlService.writeAccountingOfficeSettings(accountingOfficeData);
    }

    @Override
    public AccountingOfficeData getAccountingOfficeData() throws IOException {

        AccountingOfficeData accountingOfficeData = XmlService.readAccountingOfficeSettings();

        if (accountingOfficeData.getPassword() == null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("podaj hasło");
            accountingOfficeData.setPassword(reader.readLine());
        }

        Optional<List<UserData>> optionalUserDataList = Optional.ofNullable(accountingOfficeData.getUserDataList());
        accountingOfficeData.setUserDataList(
                optionalUserDataList.orElse(new ArrayList<>())
        );
        return accountingOfficeData;
    }

    @Override
    public boolean isNewUserInComarchDb(AccountingOfficeData accountingOfficeData, String dbName) {

        return  accountingOfficeData.getUserDataList()
                .stream()
                .noneMatch(user -> user.getDbName().equals(dbName));
    }

    @Override
    public UserData getUserData(AccountingOfficeData accountingOfficeData, String companyName) {

        return accountingOfficeData.getUserDataList().stream()
                .filter(user -> user.getCompanyName().equals(companyName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("brak użytkownika"));
    }
}
