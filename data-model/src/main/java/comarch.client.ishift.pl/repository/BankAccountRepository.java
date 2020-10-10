package comarch.client.ishift.pl.repository;

import comarch.client.ishift.pl.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("from BankAccount where id <>1")
    List<BankAccount> getAllBankAccountsList();
}
