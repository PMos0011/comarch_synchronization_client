package comarch.client.ishift.pl.dataModel.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "CDN.PlikiJPK")
public class JPKFiles {

    @Id
    @Column(name = "JPK_JPKID")
    private Long id;

    @Column(name = "JPK_StatusCode")
    private Integer statusCode;

    @Column(name = "JPK_DkNID")
    private Long declarationId;

    @OneToOne
    @JoinColumn(name = "JPK_DkNID", updatable = false, insertable = false)
    private DeclarationData declarationData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Long getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(Long declarationId) {
        this.declarationId = declarationId;
    }

    public DeclarationData getDeclarationData() {
        return declarationData;
    }

    public void setDeclarationData(DeclarationData declarationData) {
        this.declarationData = declarationData;
    }
}
