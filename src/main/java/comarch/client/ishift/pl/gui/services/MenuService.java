package comarch.client.ishift.pl.gui.services;

import comarch.client.ishift.pl.Application;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.bootstrap.Bootstrap;
import comarch.client.ishift.pl.databaseService.services.XmlService;
import comarch.client.ishift.pl.gui.MainWindow;

import javax.swing.*;
import java.io.IOException;

public class MenuService {

    public void exitApplication() {
        System.exit(0);
    }

    public void startSynchro() {
        AccountingOfficeData accountingOfficeData = MainWindow.getMainWindow().getAccountingOfficeData();
        MainWindow.getMainWindow().getContentLayout().startSynchro();
        Thread synchroThread = new Thread(() -> {
            try {
                Bootstrap.getBootstrap().makeSynchro(accountingOfficeData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        synchroThread.start();
    }

    public void showClients() {
        MainWindow.getMainWindow().displayUser();
    }

    public void showSettings() {
        AccountingOfficeData accountingOfficeData = MainWindow.getMainWindow().getAccountingOfficeData();
        try {
            accountingOfficeData = XmlService.readAccountingOfficeSettings();
            MainWindow.getMainWindow().setAccountingOfficeData(accountingOfficeData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainWindow.getMainWindow().getSideBar().displayOfficeData(accountingOfficeData);
    }
}
