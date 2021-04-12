package comarch.client.ishift.pl.gui;

import comarch.client.ishift.pl.gui.services.MenuService;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MenuBar {
    private final JMenuBar menu;

    public MenuBar() {
        this.menu = new JMenuBar();
        MenuService menuService = new MenuService();

        JMenu fileMenu = new JMenu("Plik");

        JMenuItem synchro = new JMenuItem("Synchronizuj");
        synchro.addActionListener(action -> menuService.startSynchro());

        JMenuItem clients = new JMenuItem("Klienic");
        clients.addActionListener(action -> menuService.showClients());

        JMenuItem settings = new JMenuItem("Ustawienia");
        settings.addActionListener(action -> menuService.showSettings());

        JMenuItem exit = new JMenuItem("Zamknij");
        exit.addActionListener(action -> menuService.exitApplication());

        fileMenu.add(synchro);
        fileMenu.add(clients);
        fileMenu.add(settings);
        fileMenu.add(exit);
        menu.add(fileMenu);
    }

    public JMenuBar getMenu() {
        return menu;
    }
}
