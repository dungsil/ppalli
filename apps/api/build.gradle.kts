// (c) 2022 companyname All rights reserved.
plugins {
  id("kotlin-jpa")
  id("kotlin-spring")
  id("org.springframework.boot")
  id("io.spring.dependency-management")
}

dependencies {
  // Spring boot
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  testImplementation("org.springframework.boot:spring-boot-starter-test")

  // .env
  implementation("me.paulschwarz:spring-dotenv:2.4.1")

  // JDBC
  runtimeOnly("org.postgresql:postgresql")
  testRuntimeOnly("com.h2database:h2")

  // Liquibase
  implementation("org.liquibase:liquibase-core")

  // Jwt
  implementation("com.auth0:java-jwt:3.18.3")
}
