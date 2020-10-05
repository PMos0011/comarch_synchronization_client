package comarch.client.ishift.pl.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {

    public final static String SERVER_ADDRESS = "http://localhost:8080";
    //public final static String SERVER_ADDRESS = "https://ishift.pl:8080";

    private final byte[] AUTH_DATA_BYTE = "{\"userName\":\"mb\",\"password\":\"mbmbmbmb\"}".getBytes();



    @Override
    public String sendRequest(String authToken, byte[] data, String address) throws IOException {

        Optional<String> optionalToken = Optional.ofNullable(authToken);

        authToken = optionalToken.orElseGet(() -> {
            try {
                HttpURLConnection con = setConnection("/login", null, "POST");
                sendData(con, AUTH_DATA_BYTE);
                String token = getAuthorizationHeader(con, null);
                con.disconnect();
                return token;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        if (authToken != null) {
            HttpURLConnection con = setConnection(address, authToken, "POST");
            sendData(con, data);
            authToken = getAuthorizationHeader(con, authToken);
            con.disconnect();

        }
        return authToken;
    }

    @Override
    public String sendRequest(String token, String address) throws IOException {
        if (token != null) {
            HttpURLConnection con = setConnection(address, token, "GET");
            token = getAuthorizationHeader(con, token);
            con.disconnect();
        }
        return token;
    }

    private HttpURLConnection setConnection(String address, String authToken, String method) throws IOException {

        Optional<String> authHeader = Optional.ofNullable(authToken);

        URL url = new URL(SERVER_ADDRESS + address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        authHeader.ifPresent(token -> con.setRequestProperty("Authorization", token));

        return con;
    }

    private void sendData(HttpURLConnection con, byte[] data) throws IOException {
        try (OutputStream os = con.getOutputStream()) {
            os.write(data, 0, data.length);
        }
    }

    private String getAuthorizationHeader(HttpURLConnection con, String token) throws IOException {

        if (con.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED)
            return con.getHeaderFields().get("Authorization").get(0);
        else if (con.getResponseCode() == HttpURLConnection.HTTP_CREATED
                || con.getResponseCode() == HttpURLConnection.HTTP_OK)
            return token;
        else
            return null;
    }
}
