package comarch.client.ishift.pl.repository;

import comarch.client.ishift.pl.model.CompanyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyDataRepository extends JpaRepository<CompanyData,Long> {

    @Query("from CompanyData where companyNumber = 1119")
    CompanyData getCompanyName();
}
