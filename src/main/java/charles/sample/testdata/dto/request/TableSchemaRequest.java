package charles.sample.testdata.dto.request;

import charles.sample.testdata.dto.TableSchemaDto;

import java.util.List;

public record TableSchemaRequest(
    String userId, String schemaName, List<SchemaFieldRequest> schemaFields) {
  public TableSchemaDto toDto() {
    return TableSchemaDto.of(
        userId,
        schemaName,
        null,
        schemaFields.stream()
            .map(SchemaFieldRequest::toDto)
            .collect(java.util.stream.Collectors.toUnmodifiableSet()));
  }
}
