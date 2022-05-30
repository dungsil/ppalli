
import org.gradle.api.JavaVersion.VERSION_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Gradle 루트 프로젝트 설정
// 하위 어플리케이션을 전역적으로 관리하기 위한 설정
plugins {
  // Kotlin plugin
  kotlin("jvm") version "1.6.21"
  kotlin("kapt") version "1.6.20"
  kotlin("plugin.spring") version "1.6.10"
  kotlin("plugin.jpa") version "1.6.10"

  // Spring boot plugin
  id("org.springframework.boot") version "2.6.4"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "packagename"
version = "0.0.0"

repositories {
  mavenCentral()
}

dependencies {
  // Kotlin
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))

  // Spring boot
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  kapt("org.springframework.boot:spring-boot-configuration-processor")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("me.paulschwarz:spring-dotenv:2.4.1")
  testImplementation("org.springframework.boot:spring-boot-starter-test")

  // Jackson
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  // Jwt 구현체
  implementation("com.auth0:java-jwt:3.18.3")

  // JDBC 드라이버 및 데이터베이스 도구
  implementation("org.liquibase:liquibase-core")
  runtimeOnly("org.postgresql:postgresql") // JDBC Driver
}

// 자바 컴파일 설정
java {
  sourceCompatibility = VERSION_17
  targetCompatibility = VERSION_17
}

sourceSets {
  main {
    java.srcDir("api/main/kotlin")
    resources.srcDir("api/main/resources")
  }

  test {
    java.srcDir("api/test/kotlin")
    resources.srcDir("api/test/resources")
  }
}

tasks {
  // 코틀린 컴파일 설정
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "17"
      freeCompilerArgs = listOf("-Xjsr305=strict") // `@NonNull` 어노테이션 호환
    }
  }

  // 테스트 설정
  withType<Test> {
    useJUnitPlatform()
  }
}
