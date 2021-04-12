package comarch.client.ishift.pl.gui.services;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.services.XmlService;
import comarch.client.ishift.pl.gui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SideBarServices implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JRadioButton button = (JRadioButton) e.getSource();
        MainWindow.getMainWindow().getAccountingOfficeData().getUserDataList().forEach(userData -> {
            if (userData.getDbName().equals(button.getName())) {
                userData.setSynchro(button.isSelected());
            }
        });
        MainWindow.getMainWindow().displayUser();
    }

    public void saveSettings(JPanel form) {
        AccountingOfficeData accountingOfficeData = MainWindow.getMainWindow().getAccountingOfficeData();
        JTextField name = (JTextField) form.getComponent(1);
        JTextField user = (JTextField) form.getComponent(3);
        JTextField pass = (JTextField) form.getComponent(5);
        JTextField serverAddress = (JTextField) form.getComponent(7);
        JTextField comarchDBAddress= (JTextField) form.getComponent(9);
        JTextField chomarchDBPass= (JTextField) form.getComponent(11);
        accountingOfficeData.setAccountingOfficeName(name.getText());
        accountingOfficeData.setUser(user.getText());
        accountingOfficeData.setPassword(pass.getText());
        accountingOfficeData.setServerAddress(serverAddress.getText());
        accountingOfficeData.setComarchServerAddress(comarchDBAddress.getText());
        accountingOfficeData.setComarchServerPassword(chomarchDBPass.getText());

        try {
            XmlService.writeAccountingOfficeSettings(accountingOfficeData);
            JOptionPane.showMessageDialog(null,
                    "Zapisałem, uruchom aplikacje ponownie",
                    "Settings Info",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Coś nie tak z zapisem",
                    "Settings Info",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
