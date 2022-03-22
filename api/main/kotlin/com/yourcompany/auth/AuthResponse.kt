package com.yourcompany.auth

import java.time.Instant
import java.util.*

/**
 * Jwt 토큰
 *
 * @param accessToken 엑세스 토큰
 * @param refreshToken 리프래시 토큰
 * @param expires 토큰 유효기간 (단위: 유닉스 타임)
 */
data class AuthResponse(
  val tokenType: String = "bearer",
  val accessToken: String,
  val refreshToken: String,
  val expires: Long
) {

  /**
   * Jwt 토큰
   *
   * @param accessToken 엑세스 토큰
   * @param refreshToken 리프래시 토큰
   * @param expires 토큰 유효기간 (단위: 유닉스 타임)
   */
  constructor(accessToken: String, refreshToken: UUID, expires: Instant) : this(
    accessToken = accessToken,
    refreshToken = refreshToken.toString(),
    expires = expires.toEpochMilli()
  )
}
