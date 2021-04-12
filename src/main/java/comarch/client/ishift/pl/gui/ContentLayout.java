package comarch.client.ishift.pl.gui;

import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.gui.services.ContentLayoutService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ContentLayout extends InterfaceClear {

    private final JPanel content;
    private final ContentLayoutService contentLayoutService;

    public ContentLayout() {
        content = new JPanel();
        contentLayoutService = new ContentLayoutService();

        content.setLayout(new FlowLayout(FlowLayout.LEFT));
        content.add(new JLabel("Uruchamiam..."));
    }

    public JPanel getContent() {
        return content;
    }

    public void displayUsers(List<UserData> userDataList) {
        clearView(content);
        MainWindow.getMainWindow().getSideBar().clearSideBar();

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(0, 1));
        userDataList.forEach(user -> {
            if (!user.getCompanyName().equals("")) {
                JLabel label = new JLabel();
                label.setName(user.getDbName());
                label.addMouseListener(contentLayoutService);
                label.setOpaque(true);
                label.setText(user.getCompanyName());
                if (!user.getSynchro()) {
                    label.setForeground(Color.GRAY);
                } else {
                    if (!user.getSuccessfullySynchro()) {
                        label.setForeground(Color.RED);
                    }
                }
                userPanel.add(label);
            }
        });
        JScrollPane jScrollPane = createJScrollPane(userPanel, userDataList.size() * 20, 900);
        content.add(jScrollPane);
        MainWindow.getMainWindow().revalidate();
    }

    public void startSynchro(){
        clearView(content);
        content.add(new JLabel("Synchronizuję..."));
        MainWindow.getMainWindow().revalidate();
    }

    public void displaySynchroContent(List<String> s) {
        clearView(content);
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(0, 1));
        s.forEach(line-> {
            JLabel label = new JLabel(line);
            label.setOpaque(true);
            label.setForeground(Color.GRAY);
            if(line.contains("znalazłem")){
                label.setForeground(Color.BLACK);
            }
            userPanel.add(label);
        });
        JScrollPane jScrollPane = createJScrollPane(userPanel, s.size() * 20, 900);
        content.add(jScrollPane);
        MainWindow.getMainWindow().revalidate();
    }

    public void showError(){
        clearView(content);
        content.add(new JLabel("Cos poszło nie tak. Sprawdż ustawienia, firewall"));
        MainWindow.getMainWindow().revalidate();
    }
}