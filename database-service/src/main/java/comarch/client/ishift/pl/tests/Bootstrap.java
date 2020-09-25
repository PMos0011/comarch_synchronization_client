package comarch.client.ishift.pl.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.configurations.ClientDatabaseContextHolder;
import comarch.client.ishift.pl.data.DataBasesListSingleton;
import comarch.client.ishift.pl.model.DeclarationData;
import comarch.client.ishift.pl.repository.DeclarationDataRepository;
import comarch.client.ishift.pl.repository.DeclarationDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class Bootstrap implements CommandLineRunner {

    private final DataBasesListSingleton dataBasesListSingleton;
    private final DeclarationDataRepository declarationDataRepository;
    private final DeclarationDetailsRepository declarationDetailsRepository;

    @Autowired
    public Bootstrap(DeclarationDataRepository declarationDataRepository, DeclarationDetailsRepository declarationDetailsRepository) {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null);
        this.declarationDataRepository = declarationDataRepository;
        this.declarationDetailsRepository = declarationDetailsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(dataBasesListSingleton.getDatabasesList().size());

        ClientDatabaseContextHolder.set(dataBasesListSingleton.getDatabasesList().get(0));
        List<DeclarationData> declarationData = declarationDataRepository.findAll();
        ClientDatabaseContextHolder.clear();

        ObjectMapper objectMapper = new ObjectMapper();

        String test = objectMapper.writeValueAsString(declarationData);

        System.out.println(test);

    }
}
