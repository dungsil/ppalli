// (c) 2022 companyname All rights reserved.
package yourpackage.api.auth.jwt

import java.time.Instant
import java.util.UUID

/**
 * Jwt 토큰
 *
 * @param accessToken 엑세스 토큰
 * @param refreshToken 리프래시 토큰
 * @param exp 토큰 유효기간 (단위: 유닉스 타임)
 */
data class JwtToken(
  val accessToken: String,
  val refreshToken: String,
  val exp: Long
) {

  /**
   * Jwt 토큰
   *
   * @param accessToken 엑세스 토큰
   * @param refreshToken 리프래시 토큰
   * @param exp 토큰 유효기간 (단위: 유닉스 타임)
   */
  constructor(accessToken: String, refreshToken: UUID, exp: Instant) : this(
    accessToken,
    refreshToken.toString(),
    exp.toEpochMilli()
  )
}
