package comarch.client.ishift.pl.databaseService.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.AccountingOfficeData;
import comarch.client.ishift.pl.databaseService.accountingOfficeSettings.UserData;
import comarch.client.ishift.pl.databaseService.services.HttpRequestService;
import comarch.client.ishift.pl.dataModel.model.DeclarationData;
import comarch.client.ishift.pl.dataModel.model.DeclarationDetails;
import comarch.client.ishift.pl.dataModel.repository.DeclarationDataRepository;
import comarch.client.ishift.pl.databaseService.services.DeclarationDataService;
import comarch.client.ishift.pl.databaseService.services.XmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static comarch.client.ishift.pl.databaseService.services.XmlService.writeAccountingOfficeSettings;

@Service
public class DeclarationDataServiceImpl implements DeclarationDataService {

    private final HttpRequestService httpRequestService;
    private final DeclarationDataRepository declarationDataRepository;

    @Autowired
    public DeclarationDataServiceImpl(HttpRequestService httpRequestService,
                                      DeclarationDataRepository declarationDataRepository) {
        this.httpRequestService = httpRequestService;
        this.declarationDataRepository = declarationDataRepository;
    }

    @Override
    public void sendDeclarationData(AccountingOfficeData accountingOfficeData, UserData userData) {
        try {
            httpRequestService.sendRequest(
                    new ObjectMapper().writeValueAsString(getDeclarationData(userData.getUpdateDate())),
                    "/synchro/documents/" + userData.getServerDBName(),
                    accountingOfficeData.getUser(),
                    accountingOfficeData.getPassword());

            userData.setUpdateDate(
                    new Timestamp(System.currentTimeMillis()).getTime()
            );

            writeAccountingOfficeSettings(accountingOfficeData);


        } catch (IOException e) {
            System.out.println("bład konwersji");
        } catch (RuntimeException e) {
            System.out.println(e.toString());
        }
    }

    private List<DeclarationData> getDeclarationData(Long date) {

        System.out.println(new Date(date));
        System.out.println("Sprawdzam dokumenty");

        Optional<List<DeclarationData>> declarationDataOptional = declarationDataRepository.findAllSupportedDeclarationsWithoutYearsDeclarations(new Date(date));

        List<DeclarationData> declarationData = declarationDataOptional.orElseThrow(() -> new RuntimeException("brak dokumentów"));
        System.out.println("zanlezione: " + declarationData.size());

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
