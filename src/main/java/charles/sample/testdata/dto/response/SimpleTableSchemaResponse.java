package charles.sample.testdata.dto.response;

import charles.sample.testdata.dto.TableSchemaDto;

public record SimpleTableSchemaResponse(String schemaName, String userId) {
  public static SimpleTableSchemaResponse fromDto(TableSchemaDto dto) {
    return new SimpleTableSchemaResponse(dto.schemaName(), dto.userId());
  }
}
