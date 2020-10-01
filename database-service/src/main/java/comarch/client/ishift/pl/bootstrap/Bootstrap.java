package comarch.client.ishift.pl.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.data.DataBasesListSingleton;
import comarch.client.ishift.pl.model.CompanyData;
import comarch.client.ishift.pl.model.DeclarationData;
import comarch.client.ishift.pl.model.DeclarationDetails;
import comarch.client.ishift.pl.model.TransferObject;
import comarch.client.ishift.pl.repository.CompanyDataRepository;
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
    private final CompanyDataRepository companyDataRepository;

    @Autowired
    public Bootstrap(DeclarationDataRepository declarationDataRepository,
                     HttpRequestService httpRequestService,
                     CompanyDataRepository companyDataRepository) {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null);
        this.declarationDataRepository = declarationDataRepository;
        this.httpRequestService = httpRequestService;
        this.companyDataRepository = companyDataRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        String test = null;

        for (String dbName : dataBasesListSingleton.getDatabasesList()) {
            ClientDatabaseContextHolder.set(dbName);
            CompanyData companyData = companyDataRepository.getCompanyName();
            TransferObject transferObject = new TransferObject(dbName, companyData.getCompanyData(), getData());
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
                    decl.getDeclarationDetails().stream()
                            .filter(this::detailFilter)
                            .filter(d -> d.getValue().length() > 3)
                            .filter(this::hasDotFilter)
                            .filter(d->!d.getDescription().contains("Stawka "))
                            .filter(d->!d.getDescription().contains("Rodzaj płatnika"))
                            .collect(Collectors.toList());
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

    public boolean hasDotFilter(DeclarationDetails d) {

        try {
            Character a = d.getValue().charAt(d.getValue().length() - 3);
            return a.equals('.');

        } catch (Exception e) {
            return false;
        }
    }

}
