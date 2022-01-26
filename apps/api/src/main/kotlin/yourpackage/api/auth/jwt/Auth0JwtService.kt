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
package yourpackage.api.auth.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import yourpackage.api.account.Account
import yourpackage.api.auth.session.AuthSession
import yourpackage.api.auth.session.AuthSessionRepository
import java.time.Instant
import java.time.temporal.ChronoUnit.MINUTES
import java.util.*

/**
 * [com.auth0.jwt.JWT]를 사용하는 Jwt 구현체
 */
@Service
class Auth0JwtService(
  @Value("\${api.jwt.secret}") private val secret: String,
  @Value("\${api.jwt.issuer}") private val issuer: String,
  @Value("\${api.jwt.expires}") private val expiresMinute: Long,
  private val sessions: AuthSessionRepository
) : JwtService {
  /**
   * JWT 인코딩 알고리즘
   */
  private val algorithm = Algorithm.HMAC512(secret)

  /**
   * JWT 검증 유틸리티
   */
  private val verifier = JWT.require(algorithm).withIssuer(issuer).build()

  override fun issueToken(account: Account): JwtToken {
    val expireDate = Instant.now().plus(expiresMinute, MINUTES)

    val accessToken = JWT.create()
      .withJWTId(account.id.toString())
      .withClaim("username", account.username)
      .withIssuer(issuer)
      .withIssuedAt(Date())
      .withExpiresAt(Date.from(expireDate))
      .sign(algorithm)

    // 발급된 토큰 저장
    sessions.save(
      AuthSession(
        id = account.id,
        accessToken = accessToken,
        expires = expiresMinute
      )
    )

    return JwtToken(
      accessToken = accessToken,
      exp = expireDate
    )
  }

  override fun validateToken(token: String?): Boolean {
    if (token == null) {
      return false
    }

    return sessions.findByAccessToken(token) != null
  }

  override fun getAccountIdByToken(accessToken: String): Long {
    return verifier.verify(accessToken).id.toLong()
  }

  override fun revokeToken(accessToken: String) {
    sessions.deleteByAccessToken(accessToken)
  }
}
