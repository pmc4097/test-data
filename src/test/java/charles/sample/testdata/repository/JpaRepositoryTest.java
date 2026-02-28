package charles.sample.testdata.repository;

import static org.assertj.core.api.Assertions.assertThat;

import charles.sample.testdata.domain.MockData;
import charles.sample.testdata.domain.SchemaField;
import charles.sample.testdata.domain.TableSchema;
import charles.sample.testdata.domain.constant.MockDataType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

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
  private final ObjectMapper mapper = new ObjectMapper();

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
    entityManager.clear();
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

  @Test
  void updateTest() {
    TableSchema tableSchema = tableSchemaRepository.findAll().getFirst();
    tableSchema.setSchemaName("test_modified");
    tableSchema.clearSchemaFields();
    tableSchema.addSchemaField(
        SchemaField.of(
            "age", MockDataType.NUMBER, 3, 0, getJson(Map.of("min", 1, "max", 30)), null));
    // When
    TableSchema updated = tableSchemaRepository.saveAndFlush(tableSchema);

    // Then
    assertThat(updated)
            .hasFieldOrPropertyWithValue("schemaName", "test_modified")
            .hasFieldOrPropertyWithValue("createdBy", "uno")
            .hasFieldOrPropertyWithValue("modifiedBy", TEST_AUDITOR)
            .extracting("schemaFields", InstanceOfAssertFactories.COLLECTION)
            .hasSize(1);
    assertThat(tableSchema.getCreatedAt()).isBefore(updated.getModifiedAt());
  }

  private String getJson(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException("JSON 변환 테스트 오류 발생", jpe);
    }
  }

  @Test
  void deleteTest() {
    // Give
    TableSchema tableSchema = tableSchemaRepository.findAll().getFirst();
    // When
    tableSchemaRepository.delete(tableSchema);
    // Then
    assertThat(tableSchemaRepository.count()).isEqualTo(0);
    assertThat(schemaFieldRepository.count()).isEqualTo(0);

  }

  @Test
  void insertUKConstraintTest() {
    // Give
    MockData mockData1 = MockData.of(MockDataType.NAME, "홍길동");
    MockData mockData2 = MockData.of(MockDataType.NAME, "홍길동");

    // When
    Throwable t = Assertions.catchThrowable(() -> {
      mockDataRepository.saveAll(List.of(mockData1, mockData2));
    });

    // Then
    assertThat(t).isInstanceOf(DataIntegrityViolationException.class)
            .hasCauseInstanceOf(ConstraintViolationException.class)
            .hasRootCauseInstanceOf(SQLIntegrityConstraintViolationException.class)
            .rootCause()
            .message()
            .contains("Unique index or primary key violation");
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
