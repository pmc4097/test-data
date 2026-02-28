package charles.sample.testdata.dto.response;

import charles.sample.testdata.domain.constant.MockDataType;
import charles.sample.testdata.dto.SchemaFieldDto;

public record SchemaFieldResponse(
        String fieldName,
        MockDataType mockDataType,
        Integer fieldOrder,
        Integer blankPercent,
        String typeOptionJson,
        String forceValue
) {
  public static SchemaFieldResponse fromDto(SchemaFieldDto dto) {
    return new SchemaFieldResponse(
            dto.fieldName(),
            dto.mockDataType(),
            dto.fieldOrder(),
            dto.blankPercent(),
            dto.typeOptionJson(),
            dto.forceValue()
    );
  }
}
