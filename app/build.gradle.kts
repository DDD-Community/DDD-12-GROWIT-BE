plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.dependency.management)
  alias(libs.plugins.restdocs)
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
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.boot.starter.validation)
  // json
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // log
  implementation(libs.logstash.logback.encoder) // 또는 최신 안정 버전

  // uuid
  implementation(libs.nanoid)

  // jwt
  implementation(libs.jjwt.api)
  runtimeOnly(libs.jjwt.impl)
  runtimeOnly(libs.jjwt.jackson)

  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.data.jpa)

  developmentOnly(libs.spring.boot.devtools)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)

  runtimeOnly(libs.h2)
  runtimeOnly(libs.postgresql)

  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.spring.security.test)

  testRuntimeOnly(libs.h2)
  testImplementation(libs.rest.assured)

  testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.3")
  testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.2")

}

tasks.test {
  useJUnitPlatform()
  finalizedBy("copyOasToSwagger")
}

openapi3 {
  this.setServer("http://localhost:8080")
  title = "GrowIT API"
  description = "GrowIT description"
  version = "0.0.1"
  format = "yaml" // or json
}

tasks.register<Copy>("copyOasToSwagger") {
  dependsOn("openapi3") // openapi3 실행 이후 복사
  from("$buildDir/api-spec/openapi3.yaml")
  into("src/main/resources/static/swagger-ui/")
}


