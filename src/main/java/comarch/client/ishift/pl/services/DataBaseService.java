package comarch.client.ishift.pl.services;

import java.sql.SQLException;
import java.util.List;

public interface DataBaseService {

    List<String> getAllDatabasesNameFromServer(String databaseServer) throws SQLException;
}
