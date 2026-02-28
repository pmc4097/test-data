package charles.sample.testdata.repository;

import charles.sample.testdata.domain.TableSchema;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaRepository extends JpaRepository<TableSchema, Long> {

  Page<TableSchema> findByUserId(String userId, Pageable pageable);

  Optional<TableSchema> findByUserIdAndSchemaName(String userId, String schemaName);

  void deleteByUserIdAndSchemaName(String userId, String schemaName);
}
