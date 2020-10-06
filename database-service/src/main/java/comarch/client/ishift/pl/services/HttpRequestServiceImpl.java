package comarch.client.ishift.pl.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.model.TransferObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {

    public final static String SERVER_ADDRESS = "http://localhost:8080";
    //public final static String SERVER_ADDRESS = "https://ishift.pl:8080";

    private final String AUTH_DATA_BYTE = "{\"userName\":\"mb\",\"password\":\"mbmbmbmb\"}";


    @Override
    public String sendRequest(String authToken, TransferObject transferObject, String address) throws IOException {

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
            String data = new ObjectMapper().writeValueAsString(transferObject);

            HttpURLConnection con = setConnection(address, authToken, "POST");
            sendData(con, data);
            authToken = getAuthorizationHeader(con, authToken);
            String password = getResponse(con);
            con.disconnect();

            if (!password.equals(""))
                writeNewClientAccessData(transferObject.getRegon(), password, transferObject.getCompanyName());

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

    public void writeNewClientAccessData(String login, String password, String fileName) throws IOException {
        String accessData = "login: " + login +
                " hasło: " + password;
        fileName = System.getProperty("user.dir") + "/" + fileName + ".txt";

//        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//        writer.write(accessData);
//        writer.close();

        System.out.println(accessData);
    }
}
