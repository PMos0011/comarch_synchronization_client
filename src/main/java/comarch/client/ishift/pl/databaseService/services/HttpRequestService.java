package comarch.client.ishift.pl.databaseService.services;

import java.io.IOException;

public interface HttpRequestService {

        String getAuthorization(String userName, String password);

        String sendRequest(String object, String url, String userName, String password) throws IOException;

        boolean sendRequest(String url) throws IOException;
}
