package com.growit.app.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.fake.user.ResourceFixture;
import com.growit.app.user.domain.jobrole.repository.JobRoleRepository;
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
    given(jobRoleRepository.findAll()).willReturn(ResourceFixture.defaultJobRoles());

    mockMvc
        .perform(get("/resource/jobroles"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-job-roles",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("JobRole")
                        .summary("전체 직무 목록 조회")
                        .responseFields(
                            fieldWithPath("data.jobRoles[].id").type(STRING).description("직무 ID"),
                            fieldWithPath("data.jobRoles[].name").type(STRING).description("직무 이름"))
                        .build())));
  }
}
