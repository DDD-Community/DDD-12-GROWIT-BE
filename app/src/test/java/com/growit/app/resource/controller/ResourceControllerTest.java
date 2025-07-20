package com.growit.app.resource.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.fake.resource.JobRoleFixture;
import com.growit.app.fake.resource.SayingFixture;
import com.growit.app.resource.controller.dto.request.SyncJobRolesRequest;
import com.growit.app.resource.domain.jobrole.repository.JobRoleRepository;
import com.growit.app.resource.usecase.GetSayingUseCase;
import com.growit.app.resource.usecase.SyncJobRolesUseCase;
import com.growit.app.resource.usecase.SyncSayingsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
  private ObjectMapper objectMapper = new ObjectMapper();

  @MockitoBean private JobRoleRepository jobRoleRepository;
  @MockitoBean private GetSayingUseCase getSayingUseCase;
  @MockitoBean private SyncJobRolesUseCase syncJobRolesUseCase;
  @MockitoBean private SyncSayingsUseCase syncSayingsUseCase;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void getJobRoles() throws Exception {
    given(jobRoleRepository.findAll()).willReturn(JobRoleFixture.defaultJobRoles());

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

  @Test
  void syncJobRoles() throws Exception {
    SyncJobRolesRequest request = new SyncJobRolesRequest();
    // We'll mock the use case behavior since the DTO is package-private

    mockMvc
        .perform(
            post("/resource/jobroles/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"jobRoles\":[{\"id\":\"dev\",\"name\":\"개발자\"},{\"id\":\"designer\",\"name\":\"디자이너\"}]}"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "sync-job-roles",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("JobRole")
                        .summary("직무 동기화")
                        .description("전체 직무 데이터를 요청 데이터로 덮어쓰기")
                        .requestFields(
                            fieldWithPath("jobRoles[].id").type(STRING).description("직무 ID"),
                            fieldWithPath("jobRoles[].name").type(STRING).description("직무 이름"))
                        .build())));
  }

  @Test
  void getSaying() throws Exception {
    given(getSayingUseCase.execute()).willReturn(SayingFixture.defaultSaying());

    mockMvc
        .perform(get("/resource/saying"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "get-saying",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Saying")
                        .summary("격언 조회")
                        .responseFields(
                            fieldWithPath("data.message").type(STRING).description("격언 내용"),
                            fieldWithPath("data.from").type(STRING).description("격언 출처"))
                        .build())));
  }

  @Test
  void syncSayings() throws Exception {
    mockMvc
        .perform(
            post("/resource/saying/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"sayings\":[{\"id\":\"saying1\",\"message\":\"테스트 격언\",\"author\":\"그로냥\"}]}"))
        .andExpect(status().isOk())
        .andDo(
            document(
                "sync-sayings",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Saying")
                        .summary("격언 동기화")
                        .description("전체 격언 데이터를 요청 데이터로 덮어쓰기")
                        .requestFields(
                            fieldWithPath("sayings[].id").type(STRING).description("격언 ID"),
                            fieldWithPath("sayings[].message").type(STRING).description("격언 내용"),
                            fieldWithPath("sayings[].author").type(STRING).description("격언 작성자"))
                        .build())));
  }
}
