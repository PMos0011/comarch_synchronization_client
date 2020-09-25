package comarch.client.ishift.pl.repository;

import comarch.client.ishift.pl.model.DeclarationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeclarationDataRepository  extends JpaRepository<DeclarationData, Long> {
}
