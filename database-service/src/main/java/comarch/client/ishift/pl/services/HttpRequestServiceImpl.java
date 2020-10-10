package comarch.client.ishift.pl.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {

    String authToken = null;

    public final static String SERVER_ADDRESS = "http://localhost:8080";
    //public final static String SERVER_ADDRESS = "https://ishift.pl:8080";

    //private final String AUTH_DATA_BYTE = "{\"userName\":\"mb\",\"password\":\"mmmm\"}";


    @Override
    public String sendRequest(String object, String address, String userName, String password) throws IOException {


        Optional<String> optionalToken = Optional.ofNullable(authToken);

        authToken = optionalToken.orElseGet(() -> {
            try {

                String authData = "{\"userName\":\"" +
                        userName +
                        "\",\"password\":\"" +
                        password +
                        "\"}";

                HttpURLConnection con = setConnection("/login", null, "POST");
                sendData(con, authData);
                String token = getAuthorizationHeader(con, null);
                con.disconnect();
                System.out.println(token);
                return token;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });

        String clientPassword = "";

        if (authToken != null) {

            HttpURLConnection con = setConnection(address, authToken, "POST");
            sendData(con, object);
            authToken = getAuthorizationHeader(con, authToken);
            clientPassword = getResponse(con);
            con.disconnect();

        }
        return clientPassword;
    }

    @Override
    public boolean sendRequest(String address) throws IOException {
        if (authToken != null) {
            HttpURLConnection con = setConnection(address, authToken, "GET");
            authToken= getAuthorizationHeader(con, authToken);
            con.disconnect();
        }
        //TODO
        return true;
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

    private void sendData(HttpURLConnection con, String data) throws IOException {
        byte[] dataByteArray = data.getBytes();

        try (OutputStream os = con.getOutputStream()) {
            os.write(dataByteArray, 0, dataByteArray.length);
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

    private String getResponse(HttpURLConnection con) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }
}
