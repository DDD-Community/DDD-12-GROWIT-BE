allprojects {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    maven { 
      url = uri("https://repo.spring.io/milestone")
      name = "Spring Milestone Repository"
    }
    maven { 
      url = uri("https://repo.spring.io/snapshot") 
      name = "Spring Snapshot Repository"
    }
    maven {
      url = uri("https://repo1.maven.org/maven2/")
      name = "Maven Central Mirror"
    }
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
