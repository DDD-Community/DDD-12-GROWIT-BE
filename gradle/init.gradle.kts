val ktlintVersion = "1.4.0"

initscript {
  val spotlessVersion = "6.25.0"

  repositories {
    mavenCentral()
  }

  dependencies {
    classpath("com.diffplug.spotless:spotless-plugin-gradle:$spotlessVersion")
  }
}

rootProject {
  subprojects {
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
      java {
        target("src/**/*.java")
        googleJavaFormat("1.27.0")
        removeUnusedImports()
        importOrder()
        trimTrailingWhitespace()
        endWithNewline()
      }
      format("misc") {
        target("*.md", "*.gradle", "*.yml", "*.yaml")
        trimTrailingWhitespace()
        endWithNewline()
      }
    }
  }
}
