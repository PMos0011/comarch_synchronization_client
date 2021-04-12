package comarch.client.ishift.pl.databaseService.services.implementations;

import comarch.client.ishift.pl.databaseService.services.DataBaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseServiceImpl implements DataBaseService {

    @Override
    public List<String> getAllDatabasesNameFromServer(String databaseServer, String SaPassword) throws SQLException {
        String databaseURL = "jdbc:sqlserver://" + databaseServer + "\\OPTIMA;";
        String username = "sa";
        String password = SaPassword;
        Connection connection = DriverManager.getConnection(databaseURL, username, password);
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet result = metadata.getCatalogs();

        List<String> dbNames = new ArrayList<>();

        while (result.next()) {
            if (result.getString(1).contains("CDN") && !(result.getString(1).contains("CDN_KNF_Konfiguracja"))) {
                dbNames.add(result.getString(1));
            }
        }

        return dbNames;

    }
}
