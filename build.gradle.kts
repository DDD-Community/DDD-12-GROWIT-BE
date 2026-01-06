allprojects {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    mavenCentral()
    maven { 
      url = uri("https://repo.spring.io/milestone")
      name = "Spring Milestone Repository"
    }
    maven { 
      url = uri("https://repo.spring.io/snapshot")
      name = "Spring Snapshot Repository"
    }
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
