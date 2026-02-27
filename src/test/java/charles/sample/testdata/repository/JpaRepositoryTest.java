package charles.sample.testdata.repository;

import charles.sample.testdata.domain.MockData;
import charles.sample.testdata.domain.SchemaField;
import charles.sample.testdata.domain.TableSchema;
import charles.sample.testdata.domain.constant.MockDataType;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Repository] JPA Repository Test")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class JpaRepositoryTest {

  private static final String TEST_AUDITOR = "Charles";

  @Autowired private MockDataRepository mockDataRepository;
  @Autowired private TableSchemaRepository tableSchemaRepository;
  @Autowired private SchemaFieldRepository schemaFieldRepository;

  @Autowired private TestEntityManager entityManager;

  @Test
  void selectTest() {
    // Give

    // When
    List<MockData> mockDataList = mockDataRepository.findAll();
    List<SchemaField> schemaFields = schemaFieldRepository.findAll();
    List<TableSchema> tableSchemas = tableSchemaRepository.findAll();

    // Then
    assertThat(mockDataList).size().isGreaterThan(100);

    assertThat(schemaFields)
        .hasSize(4)
        .first()
        .extracting("tableSchema")
        .isEqualTo(tableSchemas.getFirst());

    assertThat(tableSchemas)
        .hasSize(1)
        .first()
        .hasFieldOrPropertyWithValue("schemaName", "test_schema1")
        .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
        .hasSize(4);
  }

  @Test
  void insertTest() {
    // Give
    TableSchema tableSchema = TableSchema.of("test_schema2", "Charles");
    tableSchema.addSchemaFields(
        List.of(
            SchemaField.of("id", MockDataType.STRING, 1, 0, null, null),
            SchemaField.of("name", MockDataType.STRING, 2, 10, null, null),
            SchemaField.of("age", MockDataType.NUMBER, 3, 20, null, null)));
    // When
    TableSchema saved = tableSchemaRepository.save(tableSchema);

    // Then
    TableSchema newTableSchema = tableSchemaRepository.findById(saved.getId()).orElseThrow();

    assertThat(newTableSchema)
        .hasFieldOrPropertyWithValue("schemaName", "test_schema2")
        .hasFieldOrPropertyWithValue("userId", "Charles")
        .hasFieldOrPropertyWithValue("createdBy", TEST_AUDITOR)
        .hasFieldOrPropertyWithValue("modifiedBy", TEST_AUDITOR)
        .hasFieldOrProperty("createdAt")
        .hasFieldOrProperty("modifiedAt")
        .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
        .hasSize(3)
        .extracting("fieldOrder", Integer.class)
        .containsExactly(1, 2, 3);

    assertThat(newTableSchema.getCreatedAt()).isEqualTo(newTableSchema.getModifiedAt());
  }

  @EnableJpaAuditing
  @TestConfiguration
  static class TestJpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
      return () -> Optional.of(TEST_AUDITOR);
    }
  }
}
