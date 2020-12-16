package comarch.client.ishift.pl.databaseService.accountingOfficeSettings;

import java.util.List;

public class AccountingOfficeData {

    String accountingOfficeName;
    String user;
    String password;
    String comarchServerAddress;
    String comarchServerPassword;
    List<UserData> userDataList;

    public AccountingOfficeData() {
    }

    public String getAccountingOfficeName() {
        return accountingOfficeName;
    }

    public void setAccountingOfficeName(String accountingOfficeName) {
        this.accountingOfficeName = accountingOfficeName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

     public String getComarchServerAddress() {
        return comarchServerAddress;
    }

    public void setComarchServerAddress(String comarchServerAddress) {
        this.comarchServerAddress = comarchServerAddress;
    }

    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public String getComarchServerPassword() {
        return comarchServerPassword;
    }

    public void setComarchServerPassword(String comarchServerPassword) {
        this.comarchServerPassword = comarchServerPassword;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }
}
