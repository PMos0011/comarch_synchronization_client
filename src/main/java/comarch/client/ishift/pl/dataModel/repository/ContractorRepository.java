package comarch.client.ishift.pl.dataModel.repository;

import comarch.client.ishift.pl.dataModel.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractorRepository extends JpaRepository<Contractor,Long> {

    @Query("from Contractor where code <> '!NIEOKREŚLONY!'")
    List<Contractor> findAll();

}
