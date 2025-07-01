allprojects {
  repositories {
    mavenCentral()
  }
}

plugins {
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.dependency.management) apply false
  alias(libs.plugins.spotless) apply false
  alias(libs.plugins.restdocs) apply false
  alias(libs.plugins.swagger) apply false
  jacoco
}
