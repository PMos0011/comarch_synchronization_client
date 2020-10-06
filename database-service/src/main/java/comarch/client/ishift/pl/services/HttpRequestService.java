package comarch.client.ishift.pl.services;

import comarch.client.ishift.pl.model.TransferObject;

import java.io.IOException;

public interface HttpRequestService {

        String sendRequest(String token, TransferObject transferObject, String url) throws IOException;

        String sendRequest(String token, String url) throws IOException;
}
