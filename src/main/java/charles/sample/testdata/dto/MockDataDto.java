package charles.sample.testdata.dto;

import charles.sample.testdata.domain.MockData;
import charles.sample.testdata.domain.constant.MockDataType;

public record MockDataDto(Long id, MockDataType mockDataType, String mockDataValue) {

  public static MockDataDto fromEntity(MockData entity) {
    return new MockDataDto(entity.getId(), entity.getMockDataType(), entity.getMockDataValue());
  }
}
