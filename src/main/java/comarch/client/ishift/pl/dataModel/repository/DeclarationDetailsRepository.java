package comarch.client.ishift.pl.dataModel.repository;

import comarch.client.ishift.pl.dataModel.model.DeclarationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeclarationDetailsRepository extends JpaRepository<DeclarationDetails, Long> {
}
