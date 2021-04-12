package comarch.client.ishift.pl.gui;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;

import javax.swing.*;
import java.awt.*;

public class MainWindow  extends JFrame {
    private static MainWindow mainWindow;
    private final ContentLayout contentLayout;
    private final SideBar sideBar;
    private AccountingOfficeData accountingOfficeData;

    public MainWindow(){
        this.setTitle("Synchro client");
        this.setLayout(new BorderLayout(20, 0));
        this.setSize(1366, 768);

        this.contentLayout = new ContentLayout();
        this.sideBar = new SideBar();
        accountingOfficeData = null;

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(new MenuBar().getMenu(), BorderLayout.NORTH);
        this.add(contentLayout.getContent(), BorderLayout.CENTER);
        this.add(sideBar.getSideBar(),BorderLayout.EAST);

        mainWindow = this;
    }

    public AccountingOfficeData getAccountingOfficeData() {
        return accountingOfficeData;
    }

    public void setAccountingOfficeData(AccountingOfficeData accountingOfficeData) {
        this.accountingOfficeData = accountingOfficeData;
    }

    public void showWindow() {
        this.setVisible(true);
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }

    public ContentLayout getContentLayout() {
        return contentLayout;
    }

    public SideBar getSideBar() {
        return sideBar;
    }

    public void setAndDisplayUsers(AccountingOfficeData accountingOfficeData){
        this.accountingOfficeData = accountingOfficeData;
        displayUser();
    }
    public void displayUser(){
        contentLayout.displayUsers(accountingOfficeData.getUserDataList());
    }
}
