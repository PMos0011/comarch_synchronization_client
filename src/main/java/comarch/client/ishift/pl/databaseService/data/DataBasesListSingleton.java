package comarch.client.ishift.pl.databaseService.data;

import comarch.client.ishift.pl.databaseService.services.implementations.DataBaseServiceImpl;
import org.springframework.stereotype.Component;

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
                throwables.printStackTrace();
            }
        }
        return instance;
    }

    public List<String> getDatabasesList() {
        return databasesList;
    }
}
