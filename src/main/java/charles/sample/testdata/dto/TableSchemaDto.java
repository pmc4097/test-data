package charles.sample.testdata.dto;

import charles.sample.testdata.domain.TableSchema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record TableSchemaDto(
    Long id,
    String userId,
    String schemaName,
    LocalDateTime exportedAt,
    Set<SchemaFieldDto> schemaFields,
    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime modifiedAt,
    String modifiedBy) {

  public static TableSchemaDto of(
      Long id,
      String userId,
      String schemaName,
      LocalDateTime exportedAt,
      Set<SchemaFieldDto> schemaFields,
      LocalDateTime createdAt,
      String createdBy,
      LocalDateTime modifiedAt,
      String modifiedBy) {
    return new TableSchemaDto(
        id,
        userId,
        schemaName,
        exportedAt,
        schemaFields,
        createdAt,
        createdBy,
        modifiedAt,
        modifiedBy);
  }

  public static TableSchemaDto of(
      String userId,
      String schemaName,
      LocalDateTime exportedAt,
      Set<SchemaFieldDto> schemaFields) {
    return new TableSchemaDto(
        null, userId, schemaName, exportedAt, schemaFields, null, null, null, null);
  }

  public static TableSchemaDto fromEntity(TableSchema entity) {
    return new TableSchemaDto(
        entity.getId(),
        entity.getUserId(),
        entity.getSchemaName(),
        entity.getExportedAt(),
        entity.getSchemaFields().stream()
            .map(SchemaFieldDto::fromEntity)
            .collect(Collectors.toUnmodifiableSet()),
        entity.getCreatedAt(),
        entity.getCreatedBy(),
        entity.getModifiedAt(),
        entity.getModifiedBy());
  }

  public TableSchema createEntity() {
    TableSchema entity = TableSchema.of(this.schemaName, this.userId);
    entity.addSchemaFields(schemaFields.stream().map(SchemaFieldDto::createEntity).toList());

    return entity;
  }
}
