package com.growit.app.user.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
public class AuthControllerTest {

  private MockMvc mockMvc;

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
        .build();
  }

  @Test
  void getSampleByIdTest() throws Exception {
    String sampleId = "aaa";

    mockMvc.perform(get("/api/v1/samples/{sampleId}", sampleId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("sampleId", is(sampleId)))
        .andExpect(jsonPath("name", is("sample-" + sampleId)))
        .andDo(MockMvcRestDocumentationWrapper.document(
            "sample",
            new ResourceSnippetParametersBuilder()
                .tag("Sample")
                .description("Get a sample by id")
                .pathParameters(
                    parameterWithName("sampleId").description("the sample id")
                )
                .responseFields(
                    fieldWithPath("sampleId").type(STRING).description("The sample identifier."),
                    fieldWithPath("name").type(STRING).description("The name of sample.")
                )
        ));
  }
}
