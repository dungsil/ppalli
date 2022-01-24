/*
 * MIT License
 *
 * Copyright ⓒ 2022 Kim Younggeon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 */
plugins {
  // Kotlin
  kotlin("jvm") version "1.6.10" apply false
  kotlin("plugin.spring") version "1.6.10" apply false
  kotlin("plugin.jpa") version "1.6.10" apply false

  // Kotlin (Global)
  id("org.jetbrains.dokka") version "1.6.10"
  id("org.jetbrains.kotlinx.kover") version "0.5.0-RC2"

  // Spring boot
  id("org.springframework.boot") version "2.6.2" apply false
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
      jvmTarget = "11"
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
