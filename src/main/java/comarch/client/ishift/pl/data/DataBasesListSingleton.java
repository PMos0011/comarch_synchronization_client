package comarch.client.ishift.pl.data;

import comarch.client.ishift.pl.services.DataBaseServiceImpl;
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

    public static DataBasesListSingleton getInstance(String arg){
        if(instance==null){
            try {
                instance = new DataBasesListSingleton(new DataBaseServiceImpl().getAllDatabasesNameFromServer(arg));
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
