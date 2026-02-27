package charles.sample.testdata.dto.request;

import charles.sample.testdata.domain.constant.MockDataType;
import charles.sample.testdata.dto.SchemaFieldDto;

public record SchemaFieldRequest(
    String fieldName,
    MockDataType mockDataType,
    Integer fieldOrder,
    Integer blankPercent,
    String typeOptionJson,
    String forceValue) {

  public SchemaFieldDto toDto() {
    return SchemaFieldDto.of(
        fieldName,
        mockDataType,
        fieldOrder,
        blankPercent,
        typeOptionJson,
        forceValue);
  }
}
