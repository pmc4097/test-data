package charles.sample.testdata.controller;

import charles.sample.testdata.dto.request.TableSchemaRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TableSchemaController {

  @GetMapping("/table-schema")
  public String viewTableSchema(TableSchemaRequest tableSchemaRequest) {
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
  public String mySchemas() {
    return "my-schemas";
  }

  @PostMapping("/table-schema/my-schemas/{schemaName}")
  public String deleteSchema(
      @PathVariable String schemaName, RedirectAttributes redirectAttributes) {

    return "redirect:/table-schema/my-schemas";
  }

  @GetMapping("/table-schema/export")
  public ResponseEntity<String> exportTableSchema(TableSchemaRequest tableSchemaRequest) {

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt")
            .body("download complete!");
  }
}
