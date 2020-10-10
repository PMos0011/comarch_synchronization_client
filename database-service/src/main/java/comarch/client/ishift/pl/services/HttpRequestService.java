package comarch.client.ishift.pl.services;

import java.io.IOException;

public interface HttpRequestService {

        String sendRequest(String object, String url, String userName, String password) throws IOException;

        boolean sendRequest(String url) throws IOException;
}
