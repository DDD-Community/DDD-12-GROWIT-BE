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

  testImplementation("com.epages:restdocs-api-spec-mockmvc:0.15.3")

}

tasks.test {
  useJUnitPlatform()
}

openapi3 {
  this.setServer("http://localhost:8080")
  title = "My API"
  description = "My API description"
  version = "0.1.0"
  format = "yaml" // or json
}

tasks.register<Copy>("copyOasToSwagger") {
  delete("src/main/resources/static/swagger-ui/openapi3.yaml") // 기존 yaml 파일 삭제
  from("$buildDir/api-spec/openapi3.yaml") // 복제할 yaml 파일 타겟팅
  into("src/main/resources/static/swagger-ui/.") // 타겟 디렉토리로 파일 복제
  dependsOn("openapi3") // openapi3 task가 먼저 실행되도록 설정
}
