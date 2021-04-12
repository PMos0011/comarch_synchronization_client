package comarch.client.ishift.pl.dataModel.repository;

import comarch.client.ishift.pl.dataModel.model.JPKFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JPKFilesRepository extends JpaRepository<JPKFiles, Long> {

    @Query("from JPKFiles where id > ?1 and statusCode = 200 and declarationId IS NOT NULL ORDER BY id ASC")
    Optional<List<JPKFiles>> findAllSendDeclarations(Long lastId);
}
