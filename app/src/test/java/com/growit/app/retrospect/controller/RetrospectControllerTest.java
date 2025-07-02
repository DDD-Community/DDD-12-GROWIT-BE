package com.growit.app.retrospect.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.SimpleType.STRING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growit.app.common.TestSecurityUtil;
import com.growit.app.fake.retrospect.RetrospectFixture;
import com.growit.app.retrospect.controller.dto.request.CreateRetrospectRequest;
import com.growit.app.retrospect.controller.dto.request.UpdateRetrospectRequest;
import com.growit.app.retrospect.usecase.CreateRetrospectUseCase;
import com.growit.app.retrospect.usecase.UpdateRetrospectUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class, MockitoExtension.class})
@SpringBootTest
class RetrospectControllerTest {
  private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private CreateRetrospectUseCase createRetrospectUseCase;
  @MockitoBean private UpdateRetrospectUseCase updateRetrospectUseCase;

  @BeforeEach
  void setUp(
      WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentationContextProvider) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider))
            .build();
    TestSecurityUtil.setMockUser();
  }

  @Test
  void createRetrospect() throws Exception {
    CreateRetrospectRequest body = RetrospectFixture.defaultCreateRetrospectRequest();
    given(createRetrospectUseCase.execute(any())).willReturn("retrospect-id");
    mockMvc
        .perform(
            post("/retrospects")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isCreated())
        .andDo(
            document(
                "create-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 생성")
                        .requestFields(
                            fieldWithPath("goalId")
                                .type(JsonFieldType.STRING)
                                .description("목표 아이디"),
                            fieldWithPath("planId")
                                .type(JsonFieldType.STRING)
                                .description("계획 아이디"),
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("회고 내용"))
                        .responseFields(fieldWithPath("data.id").type(STRING).description("회고 ID"))
                        .build())));
  }

  @Test
  void updateRetrospect() throws Exception {
    UpdateRetrospectRequest body = RetrospectFixture.defaultUpdateRetrospectRequest();
    mockMvc
        .perform(
            put("/retrospects/{id}", "retrospect-id")
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andDo(
            document(
                "update-retrospect",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag("Retrospects")
                        .summary("회고 수정")
                        .pathParameters(parameterWithName("id").description("회고 ID"))
                        .requestFields(
                            fieldWithPath("content")
                                .type(JsonFieldType.STRING)
                                .description("회고 내용"))
                        .build())));
  }
}
