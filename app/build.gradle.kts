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
