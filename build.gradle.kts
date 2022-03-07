// (c) 2022 companyname All rights reserved.
plugins {
  // Kotlin
  kotlin("jvm") version "1.6.10" apply false
  kotlin("plugin.spring") version "1.6.10" apply false
  kotlin("plugin.jpa") version "1.6.10" apply false

  // Kotlin (Global)
  id("org.jetbrains.dokka") version "1.6.10"
  id("org.jetbrains.kotlinx.kover") version "0.5.0-RC2"

  // Spring boot
  id("org.springframework.boot") version "2.6.4" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

allprojects {
  group = "yourpackage"
  version = "0.0.0"
  buildDir = file("${rootDir}/build/${project.name}")

  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "kotlin")

  dependencies {
    "implementation"(kotlin("stdlib-jdk8"))
    "implementation"(kotlin("reflect"))
    "implementation"("io.github.microutils:kotlin-logging-jvm:2.1.21")
    "testImplementation"(kotlin("test-junit5"))
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = "17"
      freeCompilerArgs = listOf("-Xjsr305=strict")
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
  }
}

tasks.withType<Test> {
  extensions.configure<kotlinx.kover.api.KoverTaskExtension> {
    isDisabled = false
  }
}
