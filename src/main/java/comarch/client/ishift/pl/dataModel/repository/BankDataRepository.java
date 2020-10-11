package comarch.client.ishift.pl.dataModel.repository;

import comarch.client.ishift.pl.dataModel.model.BankData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDataRepository extends JpaRepository<BankData,Long> {
}
