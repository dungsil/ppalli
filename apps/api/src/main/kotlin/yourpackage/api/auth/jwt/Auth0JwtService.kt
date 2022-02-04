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
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import yourpackage.api.account.Account
import yourpackage.api.account.AccountRepository
import java.time.Instant
import java.util.Date


@Service
@Suppress("LeakingThis")
class Auth0JwtService(
  override val repo: AccountRepository,
  
  @Value("\${api.jwt.secret}")
  override val secret: String,

  @Value("\${api.jwt.issuer}")
  override val issuer: String,

  @Value("\${api.jwt.expires}")
  override val expiresMinute: Long
) : JwtService {
  /**
   * JWT 인코딩 알고리즘
   */
  private val algorithm = Algorithm.HMAC512(this.secret)

  /**
   * JWT 검증 유틸리티
   */
  private val verifier = JWT.require(algorithm).withIssuer(this.issuer).build()

  override fun issueAccessToken(account: Account, expireDate: Instant): String {
    return JWT.create()
      .withJWTId(account.id.toString())
      .withClaim("username", account.username)
      .withClaim("roles", account.roles)
      .withClaim("last_login_at", account.lastLoginAt?.toString())
      .withIssuer(issuer)
      .withIssuedAt(Date())
      .withExpiresAt(Date.from(expireDate))
      .sign(algorithm)
  }

  override fun validateToken(token: String?): Boolean {
    if (token == null) {
      return false
    }

    return try {
      verifier.verify(token) != null
    } catch (ignore: JWTVerificationException) { // Jwt 유효성 검증 실패 시 발생
      return false
    }
  }

  override fun getAccountIdByToken(accessToken: String): Long {
    return verifier.verify(accessToken).id.toLong()
  }

  override fun revokeToken(accessToken: String) {
    val id = getAccountIdByToken(accessToken)

    TODO("리프래시 토큰 제거")
  }
}
