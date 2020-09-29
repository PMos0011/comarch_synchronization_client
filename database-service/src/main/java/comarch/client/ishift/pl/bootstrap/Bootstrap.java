package comarch.client.ishift.pl.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.data.DataBasesListSingleton;
import comarch.client.ishift.pl.model.DeclarationData;
import comarch.client.ishift.pl.model.DeclarationDetails;
import comarch.client.ishift.pl.model.TransferObject;
import comarch.client.ishift.pl.repository.DeclarationDataRepository;
import comarch.client.ishift.pl.services.HttpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class Bootstrap implements CommandLineRunner {

    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final HttpRequestService httpRequestService;

    @Autowired
    public Bootstrap(DeclarationDataRepository declarationDataRepository, HttpRequestService httpRequestService) {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null);
        this.declarationDataRepository = declarationDataRepository;
        this.httpRequestService = httpRequestService;
    }

    @Override
    public void run(String... args) throws Exception {

        String test = null;

        for (String dbName : dataBasesListSingleton.getDatabasesList()) {

            ClientDatabaseContextHolder.set(dbName);
            System.out.println(dbName);
            TransferObject transferObject = new TransferObject(dbName, getData());
            ClientDatabaseContextHolder.clear();

            try {
                byte[] data = new ObjectMapper().writeValueAsBytes(transferObject);

                try {
                    test = (httpRequestService.sendRequest(test, data, "http://localhost:8080/synchro"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        httpRequestService.sendRequest(test, "http://localhost:8080/synchro");

    }


    public List<DeclarationData> getData() {
        List<DeclarationData> declarationData = declarationDataRepository.findAll();

        for (DeclarationData decl : declarationData) {
            List<DeclarationDetails> declarationDetails =
                    decl.getDeclarationDetails().stream().
                            filter(this::detailFilter).collect(Collectors.toList());
            decl.setDeclarationDetails(declarationDetails);
        }
        return declarationData;
    }

    public boolean detailFilter(DeclarationDetails d) {

        try {
            String tmp = d.getValue().replace(",", "");
            double val = Double.parseDouble(tmp);

            return val > 0.0;

        } catch (Exception e) {
            return false;
        }
    }
}
