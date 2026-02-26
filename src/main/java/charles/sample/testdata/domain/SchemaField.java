package charles.sample.testdata.domain;

import charles.sample.testdata.domain.constant.MockDataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SchemaField {

  private String fieldName;
  private MockDataType mockDataType;
  private Integer fieldOrder;
  private Integer blankPercent;
  private String typeOptionJson; //{min, max, length, dateFormat, etc}
  private String forceValue;

}
