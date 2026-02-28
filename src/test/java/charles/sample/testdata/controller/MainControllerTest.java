package charles.sample.testdata.controller;

import charles.sample.testdata.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;


@DisplayName("[Controller] MainController 테스트")
@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
record MainControllerTest(
        @Autowired  MockMvc mvc)
{
  @DisplayName("[GET] 루트 경로로 GET 요청하면, 인덱스 페이지를 반환한다.")
  @Test
  void givenNothing_whenEnteringRootPage_thenShowsMainView() throws Exception {
    // Give

    // When  Ten
    mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("/table-schema"));
  }
}
