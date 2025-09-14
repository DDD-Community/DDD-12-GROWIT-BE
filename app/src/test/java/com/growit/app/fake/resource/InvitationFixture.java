package com.growit.app.fake.resource;

public class InvitationFixture {

  public static String validInvitationRequestBody() {
    return new InvitationRequestBodyBuilder().build();
  }

  public static String invalidPhoneFormatRequestBody() {
    return new InvitationRequestBodyBuilder().phone("01012345678").build();
  }

  public static String customInvitationRequestBody(String phone) {
    return new InvitationRequestBodyBuilder()
        .phone(phone != null ? phone : "010-1234-5678")
        .build();
  }
}

class InvitationRequestBodyBuilder {
  private String phone = "010-1234-5678";

  public InvitationRequestBodyBuilder phone(String phone) {
    this.phone = phone;
    return this;
  }

  public String build() {
    return String.format(
        """
        {
          "phone": "%s"
        }
        """,
        phone);
  }
}
