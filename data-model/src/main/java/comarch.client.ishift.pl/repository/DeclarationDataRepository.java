package comarch.client.ishift.pl.repository;

import comarch.client.ishift.pl.model.DeclarationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeclarationDataRepository  extends JpaRepository<DeclarationData, Long> {

    @Query("from DeclarationData where finalna = 1 and typDeklaracji < 21 and typDeklaracji <> 11" )
    List<DeclarationData> findAllSupportedDeclarations();
    
}
