package com.yourcompany.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration
import javax.validation.constraints.Min

/**
 * 인증 프로퍼티
 *
 * @property issuer 토큰 발급자
 * @property secret 토큰 암호화 키
 * @property accessTokenExpires 엑세스 토큰 유효기간
 * @property refreshTokenExpires 리프래시 토큰 유효기간
 */
@ConstructorBinding
@ConfigurationProperties("projectname.auth")
data class AuthProperties(
  val issuer: String,

  @field:Min(512)
  val secret: String,

  val accessTokenExpires: Duration,
  val refreshTokenExpires: Duration
)
