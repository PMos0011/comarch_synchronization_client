package comarch.client.ishift.pl.gui;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.gui.services.SideBarServices;

import javax.swing.*;
import java.awt.*;

public class SideBar extends InterfaceClear {
    private final JPanel sideBar;
    private final SideBarServices sideBarServices;

    public SideBar() {
        sideBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sideBarServices = new SideBarServices();
    }

    public JPanel getSideBar() {
        return sideBar;
    }

    public void clearSideBar() {
        clearView(sideBar);
    }

    public void displayUserDetails(UserData userData) {
        clearView(sideBar);
        JPanel userDetails = new JPanel();
        userDetails.setLayout(new GridLayout(0, 1));
        userDetails.add(new Label("                                                                                                        "));
        userDetails.add(new Label("Klient"));
        userDetails.add(new Label(userData.getCompanyName()));
        JTextField login = new JTextField("login: " + userData.getLogin());
        login.setEditable(false);
        userDetails.add(login);
        JTextField pass = new JTextField("hasło: " + userData.getPassword());
        pass.setEditable(false);
        userDetails.add(pass);
        JRadioButton radioButton = new JRadioButton("synchronizacja");
        radioButton.setName(userData.getDbName());
        radioButton.setSelected(userData.getSynchro());
        radioButton.addActionListener(sideBarServices);
        userDetails.add(radioButton);
        sideBar.add(userDetails);
        MainWindow.getMainWindow().revalidate();
    }

    public void displayOfficeData(AccountingOfficeData accountingOfficeData) {
        clearView(sideBar);
        JPanel userDetails = new JPanel();
        userDetails.setLayout(new GridLayout(0, 1));
        userDetails.add(new JLabel("nazwa"));
        JTextField officeName = new JTextField(accountingOfficeData.getAccountingOfficeName());
        userDetails.add(officeName);
        userDetails.add(new JLabel("użytkownik"));
        JTextField user = new JTextField(accountingOfficeData.getUser());
        userDetails.add(user);
        userDetails.add(new JLabel("hasło"));
        JTextField pass = new JTextField(accountingOfficeData.getPassword());
        userDetails.add(pass);
        userDetails.add(new JLabel("adres serwera"));
        JTextField serverIShift = new JTextField(accountingOfficeData.getServerAddress());
        userDetails.add(serverIShift);
        userDetails.add(new JLabel("adres serwera bazy danych comarch"));
        JTextField comarchDB = new JTextField(accountingOfficeData.getComarchServerAddress());
        userDetails.add(comarchDB);
        userDetails.add(new JLabel("hasło bazy danych comarch"));
        JTextField comarchPass = new JTextField(accountingOfficeData.getComarchServerPassword());
        userDetails.add(comarchPass);
        JButton button = new JButton("Zapisz");
        button.addActionListener(action -> sideBarServices.saveSettings(userDetails));
        userDetails.add(button);
        sideBar.add(userDetails);
        MainWindow.getMainWindow().revalidate();
    }
}
