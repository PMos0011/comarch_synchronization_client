package comarch.client.ishift.pl.databaseService.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.dataModel.model.JPKFiles;
import comarch.client.ishift.pl.dataModel.repository.JPKFilesRepository;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.bootstrap.SynchroEventList;
import comarch.client.ishift.pl.databaseService.services.HttpRequestService;
import comarch.client.ishift.pl.dataModel.model.DeclarationData;
import comarch.client.ishift.pl.dataModel.model.DeclarationDetails;
import comarch.client.ishift.pl.dataModel.repository.DeclarationDataRepository;
import comarch.client.ishift.pl.databaseService.services.DeclarationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static comarch.client.ishift.pl.databaseService.bootstrap.SynchroEventList.synchroEventList;

@Service
public class DeclarationDataServiceImpl implements DeclarationDataService {

    private final HttpRequestService httpRequestService;
    private final DeclarationDataRepository declarationDataRepository;
    private final JPKFilesRepository jpkFilesRepository;

    @Autowired
    public DeclarationDataServiceImpl(HttpRequestService httpRequestService,
                                      DeclarationDataRepository declarationDataRepository,
                                      JPKFilesRepository jpkFilesRepository) {
        this.httpRequestService = httpRequestService;
        this.declarationDataRepository = declarationDataRepository;
        this.jpkFilesRepository = jpkFilesRepository;
    }

    @Override
    public void sendDeclarationData(AccountingOfficeData accountingOfficeData, UserData userData) throws IOException {
                httpRequestService.sendRequest(
                        new ObjectMapper().writeValueAsString(getDeclarationData(userData)),
                        "/synchro/documents/" + userData.getServerDBName(),
                        accountingOfficeData.getUser(),
                        accountingOfficeData.getPassword());
    }

    private List<DeclarationData> getDeclarationData(UserData userData) {

        System.out.println("Sprawdzam dokumenty");
        List<DeclarationData> declarationData = declarationDataRepository
                .findAllSupportedDeclarationsWithoutYearsDeclarations(new Date(userData.getUpdateDate())).orElse(new ArrayList<>());

        userData.setUpdateDate(
                new Timestamp(System.currentTimeMillis()).getTime()
        );

        Optional<List<JPKFiles>> jpkDeclarationOptional = jpkFilesRepository.findAllSendDeclarations(userData.getJpkFileId());

        jpkDeclarationOptional.ifPresent(jpkFiles -> {
            jpkFiles.forEach(file -> declarationData.add(file.getDeclarationData()));
            userData.setJpkFileId(jpkFiles.get(jpkFiles.size() - 1).getId());
        });

        if (declarationData.size() > 0) {
            System.out.println("zanlezione: " + declarationData.size());
            synchroEventList.add("znalazłem: " + declarationData.size());

            for (DeclarationData decl : declarationData) {
                List<DeclarationDetails> declarationDetails =
                        decl.getDeclarationDetails().stream()
                                .filter(this::detailFilter)
                                .filter(d -> d.getValue().length() > 3)
                                .filter(this::hasDotFilter)
                                .filter(d -> !d.getDescription().contains("Rodzaj płatnika"))
                                .collect(Collectors.toList());
                decl.setDeclarationDetails(declarationDetails);
            }
            return declarationData;
        }
        throw new RuntimeException("brak dokumentów");
    }

    private boolean detailFilter(DeclarationDetails d) {

        try {
            String tmp = d.getValue().replace(",", "");
            double val = Double.parseDouble(tmp);

            return val >= 0.0;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasDotFilter(DeclarationDetails d) {

        try {
            Character a = d.getValue().charAt(d.getValue().length() - 3);
            return a.equals('.');

        } catch (Exception e) {
            return false;
        }
    }
}
