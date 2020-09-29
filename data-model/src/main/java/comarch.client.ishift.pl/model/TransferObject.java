package comarch.client.ishift.pl.model;

import java.util.List;

public class TransferObject {

    private String dbName;
    private List<DeclarationData> declarationData;

    public TransferObject(String dbName, List <DeclarationData> declarationData) {
        this.dbName = dbName;
        this.declarationData = declarationData;
    }

    public String getDbName() {
        return dbName;
    }

    public List<DeclarationData> getDeclarationData() {
        return declarationData;
    }
}
