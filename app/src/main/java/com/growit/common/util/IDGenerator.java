package com.growit.common.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class IDGenerator {

  public static String generateId() {
    return NanoIdUtils.randomNanoId();
  }
}
