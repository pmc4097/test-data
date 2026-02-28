package charles.sample.testdata.controller;

import charles.sample.testdata.domain.constant.ExportFileType;
import charles.sample.testdata.domain.constant.MockDataType;
import charles.sample.testdata.dto.request.TableSchemaExportRequest;
import charles.sample.testdata.dto.request.TableSchemaRequest;
import charles.sample.testdata.dto.response.SchemaFieldResponse;
import charles.sample.testdata.dto.response.SimpleTableSchemaResponse;
import charles.sample.testdata.dto.response.TableSchemaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class TableSchemaController {

  private final ObjectMapper mapper;

  @GetMapping("/table-schema")
  public String tableSchema(@RequestParam(required = false) String schemaName, Model model) {

    var tableSchema = defaultTableSchema(schemaName);
    model.addAttribute("tableSchema", tableSchema);
    model.addAttribute("mockDataTypes", MockDataType.toObjects());
    model.addAttribute("fileTypes", Arrays.stream(ExportFileType.values()).toList());

    return "table-schema";
  }

  @PostMapping("/table-schema")
  public String createOrUpdateTableSchema(
      TableSchemaRequest tableSchemaRequest, RedirectAttributes redirectAttributes) {

    // redirectAttributes.addAttribute() query string으로 전달됨
    redirectAttributes.addFlashAttribute(
        "tableSchemaRequest", tableSchemaRequest); // session에 저장되고, 한 번만 사용됨

    return "redirect:/table-schema";
  }

  @GetMapping("/table-schema/my-schemas")
  public String mySchemas(Model model) {
    var tableSchemas = mySampleSchemas();
    model.addAttribute("tableSchemas", tableSchemas);
    return "my-schemas";
  }


  @PostMapping("/table-schema/my-schemas/{schemaName}")
  public String deleteSchema(
      @PathVariable String schemaName, RedirectAttributes redirectAttributes) {

    return "redirect:/table-schema/my-schemas";
  }

  @GetMapping("/table-schema/export")
  public ResponseEntity<String> exportTableSchema(TableSchemaExportRequest exportRequest) {

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt")
        .body(json(exportRequest));
  }

  private String json(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private @NonNull TableSchemaResponse defaultTableSchema(String schemaName) {
    return new TableSchemaResponse(
            schemaName != null ? schemaName : "scheme_name",
        "Uno",
        List.of(
            new SchemaFieldResponse("id", MockDataType.STRING, 1, 0, null, null),
            new SchemaFieldResponse("age", MockDataType.NUMBER, 1, 0, null, null),
            new SchemaFieldResponse("name", MockDataType.NAME, 1, 0, null, null)));
  }
  private static @NonNull List<SimpleTableSchemaResponse> mySampleSchemas() {
    return List.of(
            new SimpleTableSchemaResponse("schema1", "홍길동", LocalDate.of(2026, 1, 1).atStartOfDay()),
            new SimpleTableSchemaResponse("schema2", "홍길동", LocalDate.of(2026, 1, 1).atStartOfDay()),
            new SimpleTableSchemaResponse("schema3", "홍길동", LocalDate.of(2026, 1, 1).atStartOfDay()));
  }
}
