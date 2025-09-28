package com.growit.app.common.docs;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.SimpleType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;

public class FieldBuilder {
  private final List<FieldDescriptor> fields = new ArrayList<>();
  
  public static FieldBuilder create() {
    return new FieldBuilder();
  }
  
  public FieldBuilder addSuccessMessageResponse() {
    fields.addAll(Arrays.asList(CommonFields.SUCCESS_MESSAGE_RESPONSE));
    return this;
  }
  
  public FieldBuilder addConsentFields() {
    fields.addAll(Arrays.asList(CommonFields.CONSENT_FIELDS));
    return this;
  }
  
  public FieldBuilder addTokenResponse() {
    fields.addAll(Arrays.asList(CommonFields.TOKEN_RESPONSE));
    return this;
  }
  
  public FieldBuilder addFields(FieldDescriptor... customFields) {
    fields.addAll(Arrays.asList(customFields));
    return this;
  }
  
  public FieldBuilder addField(String path, SimpleType type, String description) {
    fields.add(fieldWithPath(path).type(type).description(description));
    return this;
  }
  
  public FieldBuilder addOptionalField(String path, SimpleType type, String description) {
    fields.add(fieldWithPath(path).type(type).description(description).optional());
    return this;
  }
  
  public FieldDescriptor[] build() {
    return fields.toArray(new FieldDescriptor[0]);
  }
}