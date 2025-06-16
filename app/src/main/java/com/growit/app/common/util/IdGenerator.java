package com.growit.app.common.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public final class IdGenerator {
  private IdGenerator() {
  }

  public static String generateId() {
    return NanoIdUtils.randomNanoId();
  }
}
