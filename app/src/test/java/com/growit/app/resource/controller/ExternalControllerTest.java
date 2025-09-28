package com.growit.app.resource.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.growit.app.common.config.TestSecurityConfig;
import com.growit.app.resource.controller.ResourceDocumentFields;
import com.growit.app.fake.resource.InvitationFixture;
import com.growit.app.resource.usecase.CreateInvitationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ExternalControllerTest {
  private static final String TAG = ResourceDocumentFields.INVITATION_TAG;

  private MockMvc mockMvc;

  @MockitoBean private CreateInvitationUseCase createInvitationUseCase;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build();
  }

  @Test
  void createInvitation() throws Exception {
    String requestBody = InvitationFixture.validInvitationRequestBody();

    mockMvc
        .perform(
            post("/externals/invitations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andDo(
            document(
                "create-invitation",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .summary("초대장 요청 생성")
                        .requestFields(ResourceDocumentFields.INVITATION_REQUEST_FIELDS)
                        .responseFields(ResourceDocumentFields.INVITATION_RESPONSE_FIELDS)
                        .build())));
  }
}
