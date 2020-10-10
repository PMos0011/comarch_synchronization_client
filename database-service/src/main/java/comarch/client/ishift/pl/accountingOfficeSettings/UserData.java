package comarch.client.ishift.pl.accountingOfficeSettings;

public class UserData {
    String login;
    String password;
    String companyName;
    Long updateDate;

    public UserData() {
    }

    public UserData(String login, String password, String companyName, Long updateDate) {
        this.login = login;
        this.password = password;
        this.companyName = companyName;
        this.updateDate = updateDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
}
