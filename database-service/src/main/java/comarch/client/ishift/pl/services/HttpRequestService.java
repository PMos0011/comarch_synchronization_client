package comarch.client.ishift.pl.services;

import java.io.IOException;
import java.net.MalformedURLException;

public interface HttpRequestService {

        String sendRequest(String token, byte[] data, String url) throws IOException;

        String sendRequest(String token, String url) throws IOException;
}
