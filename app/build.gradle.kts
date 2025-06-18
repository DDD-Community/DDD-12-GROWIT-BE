plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.dependency.management)
  jacoco
}

group = "com.growit.app"
version = "0.0.1"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.data.jpa)
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.boot.starter.validation)


  developmentOnly(libs.spring.boot.devtools)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)

  runtimeOnly(libs.h2)
  runtimeOnly(libs.postgresql)

  testImplementation(libs.spring.boot.starter.test)
  testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
  useJUnitPlatform()
}
