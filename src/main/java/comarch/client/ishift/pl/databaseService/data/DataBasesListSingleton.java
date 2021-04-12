package comarch.client.ishift.pl.databaseService.data;

import comarch.client.ishift.pl.databaseService.services.implementations.DataBaseServiceImpl;
import comarch.client.ishift.pl.gui.MainWindow;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

@Component
public class DataBasesListSingleton {

    private static DataBasesListSingleton instance = null;
    private final List<String> databasesList;

    private DataBasesListSingleton(List<String> databasesList){
        this.databasesList=databasesList;
    };

    public static DataBasesListSingleton getInstance(String dbServerName, String saPassword){
        if(instance==null){
            try {
                instance = new DataBasesListSingleton(new DataBaseServiceImpl().getAllDatabasesNameFromServer(dbServerName,saPassword));
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(null,
                        "Błąd z połączeniem bazą Comarch",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                MainWindow.getMainWindow().getContentLayout().showError();
                throwables.printStackTrace();
            }
        }
        return instance;
    }

    public List<String> getDatabasesList() {
        return databasesList;
    }
}
