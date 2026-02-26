package charles.sample.testdata.repository;

import charles.sample.testdata.domain.MockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataRepository extends JpaRepository<MockData, Long> {

}
