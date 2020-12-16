package comarch.client.ishift.pl.dataModel.repository;

import comarch.client.ishift.pl.dataModel.model.CompanyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyDataRepository extends JpaRepository<CompanyData,Long> {

    @Query("from CompanyData where companyNumber = 1119")
    CompanyData getCompanyName();

    @Query("from CompanyData where id between 169 and 186")
    List<CompanyData> getCompanyData();

    @Query("from CompanyData where id = 169")
    CompanyData getCompanyNIP();

    @Query("from CompanyData where companyNumber = 3145")
    CompanyData getCompanyIndividualTaxAccount();

}
