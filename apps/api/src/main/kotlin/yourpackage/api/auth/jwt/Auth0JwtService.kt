// (c) 2022 companyname All rights reserved.
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
