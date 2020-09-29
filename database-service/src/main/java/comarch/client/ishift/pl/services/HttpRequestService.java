package comarch.client.ishift.pl.services;

import java.io.IOException;
import java.net.MalformedURLException;

public interface HttpRequestService {

        String postRequest(String token, byte[] data, String url) throws IOException;
}
