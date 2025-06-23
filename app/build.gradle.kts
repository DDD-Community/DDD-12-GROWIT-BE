plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.dependency.management)
  alias(libs.plugins.restdocs)
  jacoco
}

group = "com.growit.app"
version = "0.0.2"

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
  implementation(libs.nanoid)
  implementation(libs.query.dsl)
  annotationProcessor(libs.query.dsl.apt)

  // jwt
  implementation(libs.jjwt.api)
  runtimeOnly(libs.jjwt.impl)
  runtimeOnly(libs.jjwt.jackson)

  developmentOnly(libs.spring.boot.devtools)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)

  runtimeOnly(libs.h2)
  runtimeOnly(libs.postgresql)

  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.spring.security.test)

  testRuntimeOnly(libs.h2)
  testImplementation(libs.rest.assured)

  testImplementation(libs.restdocs.mockmvc)
  testImplementation(libs.restdocs.api.spec)
}

tasks.test {
  useJUnitPlatform()
  finalizedBy("copyOasToSwagger")
}

openapi3 {
  this.setServer("http://growit-alb-alb-549641300.ap-northeast-2.elb.amazonaws.com/")
  title = "GrowIT API Specification"
  description = "GrowIT description"
  version = project.version.toString()
  format = "yaml" // or json
}

tasks.register<Copy>("copyOasToSwagger") {
  dependsOn("openapi3") // openapi3 실행 이후 복사
  from("$buildDir/api-spec/openapi3.yaml")
  into("src/main/resources/static/swagger-ui/")
}


// Ensure that the build task depends on copyOpenApiYaml
tasks.named("build") {
  dependsOn("copyOasToSwagger")
}

tasks.withType<com.epages.restdocs.apispec.gradle.OpenApi3Task> {
  outputs.cacheIf { false }
}

sourceSets {
  main {
    java {
      srcDir("build/generated/sources/annotationProcessor/java/main")
    }
  }
}
