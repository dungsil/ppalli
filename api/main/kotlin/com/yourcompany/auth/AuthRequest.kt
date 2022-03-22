package com.yourcompany.auth

import org.hibernate.validator.constraints.Length

/**
 * 인증 요청 VO
 *
 * @property username 사용자 이름
 * @property password 사용자 비밀번호
 */
data class AuthRequest(
  @field:Length(min = 3, max = 100)
  val username: String,

  @field:Length(min = 8)
  val password: String
)
