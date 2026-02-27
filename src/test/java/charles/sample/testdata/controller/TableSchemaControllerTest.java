package charles.sample.testdata.controller;

import charles.sample.testdata.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[Controller] 테이블 스키마 컨트롤러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest
public record TableSchemaControllerTest(@Autowired MockMvc mvc) {

  @DisplayName("[GET] 테이블 스키마 페이지 -> 테이블 스키마 뷰 (정상)")
  @Test
  void givenNothing_whenRequesting_thenShowsTableSchemaView() throws Exception {
    // Given

    // When && Then
    mvc.perform(get("/table-schema"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(view().name("table-schema"));
  }

  @DisplayName("[POST] 테이블 스키마 생성, 변경 (정상)")
  @Test
  void givenTableSchemaData_whenCreatingOrUpdating_thenRedirectsToTableSchemaView() throws Exception {
    // Given


    // When && Then
    mvc.perform(post("/table-schema")
                    .content("Sample data")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .with(csrf())
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/table-schema"));
  }

  @DisplayName("[GET] 내 스키마 목록 페이지 -> 내 스키마 목록 뷰 (정상)")
  @Test
  void givenAuthenticatedUser_whenRequestingMySchemas_thenShowsMySchemasView() throws Exception {
    // Give

    // When &&Then
    mvc.perform(get("/table-schema/my-schemas"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("my-schemas"));
  }

  @DisplayName("[POST] 내 스키마 삭제 (정상)")
  @Test
    void givenAuthenticatedUserAndSchemaName_whenDeletingMySchema_thenRedirectsToTableSchemasView() throws Exception {
        // Give
        String schemaName = "test_schema";
        // When && Then
        mvc.perform(post("/table-schema/my-schemas/{schemaName}", schemaName)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-schemas"));
    }

    @DisplayName("[GET] 테이블 스키마 파일 다운로드 -> 파일 다운로드 응답 (정상)")
    @Test
    void givenTableSchemaName_whenDownload_thenReturnsFile() throws Exception {
    // Give

    // When && Then
    mvc.perform(get("/table-schema/export"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table-schema.txt"))
        .andExpect(content().string("download complete!"));
    }
}
