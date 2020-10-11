package comarch.client.ishift.pl.databaseService.services;

import java.sql.SQLException;
import java.util.List;

public interface DataBaseService {

    List<String> getAllDatabasesNameFromServer(String databaseServer) throws SQLException;
}
