package charles.sample.testdata.dto.request;

import charles.sample.testdata.domain.constant.ExportFileType;
import charles.sample.testdata.dto.TableSchemaDto;
import java.util.List;

public record TableSchemaExportRequest(
        String schemaName, Integer rowCount, ExportFileType fileType, List<SchemaFieldRequest> schemaFields) {

  public static TableSchemaExportRequest of(
       String schemaName, Integer rowCount, ExportFileType fileType, List<SchemaFieldRequest> schemaFields
  ) {
    return new TableSchemaExportRequest(schemaName,rowCount,fileType, schemaFields);
  }

  public TableSchemaDto toDto(String userId) {
    return TableSchemaDto.of(
        userId,
        schemaName,
        null,
        schemaFields.stream()
            .map(SchemaFieldRequest::toDto)
            .collect(java.util.stream.Collectors.toUnmodifiableSet()));
  }
}
