package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.user.domain.jobrole.JobRole;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class ResourceControllerTest {

  private MockMvc mockMvc;

  @MockitoBean private JobRoleRepository jobRoleRepository;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void getJobRolesTest() throws Exception {
    JobRole dev = new JobRole("dev", "개발자");
    JobRole designer = new JobRole("designer", "디자이너");
    JobRole planner = new JobRole("planner", "기획자");

    given(jobRoleRepository.findAll()).willReturn(Arrays.asList(dev, designer, planner));

    mockMvc
        .perform(get("/resource/jobroles"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-job-roles",
                new ResourceSnippetParametersBuilder()
                    .tag("JobRole")
                    .description("전체 직무 목록 조회")
                    .responseFields(
                        fieldWithPath("data[].id").type(STRING).description("직무 ID"),
                        fieldWithPath("data[].name").type(STRING).description("직무 이름"))));
  }
}
