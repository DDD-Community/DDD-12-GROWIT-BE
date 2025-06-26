package com.growit.app.common;

import com.growit.app.fake.user.UserFixture;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestSecurityUtil {

  public static void setMockUser() {
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(UserFixture.defaultUser(), null, List.of()));
  }

  public static void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }
}
