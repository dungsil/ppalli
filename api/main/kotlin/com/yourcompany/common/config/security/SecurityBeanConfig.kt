package com.yourcompany.common.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * 보안 관련 Bean 등록을 위한 설정
 */
@Configuration
class SecurityBeanConfig(@Value("\${spring.profiles.active}") private val profiles: List<String>) {

  /**
   * 비밀번호 암호화도구
   */
  @Bean
  fun passwordEncoder(): PasswordEncoder {
    val encoders = mutableMapOf(
      "argon2" to Argon2PasswordEncoder(), // ReadableProjectname 기본 인코더
      "bcrypt" to BCryptPasswordEncoder() // Spring security 기본 인코더
    )

    if (profiles.contains("dev")) {
      // 개발 환경에서는 암호화를 사용하지 않는다.
      @Suppress("DEPRECATION")
      encoders["noop"] = NoOpPasswordEncoder.getInstance()
    }

    return DelegatingPasswordEncoder("argon2", encoders)
  }
}
