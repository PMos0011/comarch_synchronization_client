package comarch.client.ishift.pl.tests;

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
import java.util.concurrent.atomic.AtomicReference;
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

    private final byte[] AUTH_DATA_BYTE = "{\"userName\":\"admin\",\"password\":\"admin\"}".getBytes();

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
                    test = (httpRequestService.postRequest(test, data, "http://localhost:8080/test1"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }


    public List<DeclarationData> getData() {
        System.out.println(dataBasesListSingleton.getDatabasesList().size());

        ClientDatabaseContextHolder.set(dataBasesListSingleton.getDatabasesList().get(0));
        List<DeclarationData> declarationData = declarationDataRepository.findAll();
        ClientDatabaseContextHolder.clear();

        for (DeclarationData decl : declarationData) {
            List<DeclarationDetails> declarationDetails =
                    decl.getDeclarationDetails().stream().
                            filter(this::detailFilter).collect(Collectors.toList());
            decl.setDeclarationDetails(declarationDetails);
        }

        return declarationData;

        // return new ObjectMapper().writeValueAsString(declarationData);
        //System.out.println(test);
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
