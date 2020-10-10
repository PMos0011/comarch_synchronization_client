package comarch.client.ishift.pl.repository;

import comarch.client.ishift.pl.model.BankData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDataRepository extends JpaRepository<BankData,Long> {
}
