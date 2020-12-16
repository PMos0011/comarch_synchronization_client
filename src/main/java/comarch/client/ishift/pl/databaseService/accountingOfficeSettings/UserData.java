package comarch.client.ishift.pl.databaseService.accountingOfficeSettings;

public class UserData {
    private String login;
    private String password;
    private String companyName;
    private String dbName;
    private String serverDBName;
    private Long updateDate;
    private Boolean synchro;
    private Boolean successfullySynchro;

    public UserData() {
    }

    public UserData(String companyName,  String dbName, Boolean synchro, Boolean successfullySynchro) {
        this.companyName = companyName;
        this.dbName =  dbName;
        this.synchro = synchro;
        this.successfullySynchro = successfullySynchro;
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getServerDBName() {
        return serverDBName;
    }

    public void setServerDBName(String serverDBName) {
        this.serverDBName = serverDBName;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getSynchro() {
        return synchro;
    }

    public void setSynchro(Boolean synchro) {
        this.synchro = synchro;
    }

    public Boolean getSuccessfullySynchro() {
        return successfullySynchro;
    }

    public void setSuccessfullySynchro(Boolean successfullySynchro) {
        this.successfullySynchro = successfullySynchro;
    }
}
