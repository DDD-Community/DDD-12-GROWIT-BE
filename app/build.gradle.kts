import org.hidetake.gradle.swagger.generator.GenerateSwaggerUI
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.dependency.management)
  alias(libs.plugins.restdocs)
  alias(libs.plugins.swagger)

  jacoco
}

group = "com.growit.app"
version = "0.0.3"

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

dependencies {
  implementation(libs.spring.boot.starter.web)
  implementation(libs.spring.boot.starter.webflux)
  implementation(libs.spring.boot.starter.actuator)
  implementation(libs.spring.boot.starter.data.jpa)
  implementation(libs.spring.boot.starter.security)
  implementation(libs.spring.boot.starter.validation)
  implementation(libs.spring.boot.starter.cache)
  implementation(libs.nanoid)
  implementation(libs.spring.boot.starter.batch)
  // oauth
  implementation (libs.spring.boot.starter.security.oauth2)
  // Flyway
  implementation(libs.flyway)
  implementation(libs.flyway.postgresql)

  // QueryDSL 의존성 추가
  implementation("${libs.querydsl.jpa.jakarta.get()}:jakarta")
  annotationProcessor("${libs.querydsl.apt.jakarta.get()}:jakarta")
  annotationProcessor(libs.jakarta.persistence)
  annotationProcessor(libs.jakarta.annotation)
  // jwt
  implementation(libs.jjwt.api)
  runtimeOnly(libs.jjwt.impl)
  runtimeOnly(libs.jjwt.jackson)

  // log
  implementation(libs.logstash.logback.encoder) // 또는 최신 안정 버전

  // caffeine cahce
  implementation(libs.caffeine)

  developmentOnly(libs.spring.boot.devtools)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)

  runtimeOnly(libs.h2)
  runtimeOnly(libs.postgresql)

  testImplementation(libs.spring.boot.starter.test)
  testImplementation(libs.spring.security.test)
  testImplementation(libs.spring.batch.test)

  testRuntimeOnly(libs.h2)
  testImplementation(libs.rest.assured)

  testImplementation(libs.restdocs.mockmvc)
  testImplementation(libs.restdocs.api.spec)

  swaggerUI(libs.swagger.ui)
}

tasks.test {
  useJUnitPlatform()
}


// swagger

swaggerSources {
  create("sample") {
    setInputFile(layout.buildDirectory.file("api-spec/openapi3.yaml").get().asFile)
  }
}

openapi3 {
//  this.setServer("http://localhost:8081/")
  this.setServer("https://api.grow-it.me/")
  title = "GrowIT API Specification"
  description = "GrowIT description"
  version = project.version.toString()
  format = "yaml" // or json
}

// JWT (Bearer Token)를 Authorization 헤더에 명시할 수 있게 설정
tasks.withType<GenerateSwaggerUI>().configureEach {
  dependsOn("openapi3")

  doFirst {
    val swaggerUIFile = layout.buildDirectory.file("api-spec/openapi3.yaml").get().asFile

    val securitySchemesContent = """
      |  securitySchemes:
      |    bearerAuth:
      |      type: http
      |      scheme: bearer
      |      bearerFormat: JWT
      |      name: Authorization
      |      in: header
      |      description: "Use 'your-access-token' as the value of the Authorization header"
      |security:
      |  - bearerAuth: []
    """.trimMargin()

    swaggerUIFile.appendText("\n$securitySchemesContent")
  }
}

// 생성된 Swagger UI 파일들 복사
tasks.register<Copy>("copyDocument") {
  dependsOn("generateSwaggerUISample")

  from("build/swagger-ui-sample/")
  into("src/main/resources/static/docs")
}

tasks.named<BootJar>("bootJar") {}

val generatedSrcDir = "src/main/generated"

// sourceSets 에 generated 소스 디렉터리 추가
sourceSets {
  named("main") {
    java {
      srcDir(generatedSrcDir)
    }
  }
}

tasks.withType<JavaCompile> {
  options.generatedSourceOutputDirectory.set(file(generatedSrcDir))
}

tasks.named<Delete>("clean") {
  delete(generatedSrcDir)
}
