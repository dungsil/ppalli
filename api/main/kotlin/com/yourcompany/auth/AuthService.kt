package com.yourcompany.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.yourcompany.account.Account
import com.yourcompany.account.AccountRepository
import com.yourcompany.auth.exception.ExpireTokenException
import com.yourcompany.auth.exception.LockedAccountException
import com.yourcompany.auth.exception.LoginFailException
import com.yourcompany.common.extension.getClientIp
import com.yourcompany.common.extension.getHttpServletRequest
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.Instant.now
import java.util.Date
import java.util.UUID
import javax.transaction.Transactional


@Service
@EnableConfigurationProperties(AuthProperties::class)
class AuthService(
  prop: AuthProperties,

  val sessions: AuthSessionRepository,
  val accounts: AccountRepository,
  val passwordEncoder: PasswordEncoder
) {
  /**
   * JWT 토큰 발급자
   */
  private val issuer = prop.issuer

  /**
   * JWT 유효기간 (분)
   */
  private val expires = prop.accessTokenExpires
  private val refreshExpires = prop.refreshTokenExpires

  /**
   * JWT 인코딩 알고리즘
   */
  private val algorithm = Algorithm.HMAC512(prop.secret)

  /**
   * JWT 검증 유틸리티
   */
  private val verifier = JWT.require(algorithm).withIssuer(issuer).build()

  /**
   * 로그인
   *
   * @param username 사용자 이름
   * @param rawPassword 사용자 비밀번호
   * @return 사용자 엔티티
   */
  fun login(username: String, rawPassword: String): Account {
    val account = accounts.findByUsernameAndEnableIsTrue(username) ?: throw LoginFailException()

    // 계정이 잠김 상태임
    if (account.password.isLocked()) {
      throw LockedAccountException()
    }

    // 잘못된 비밀번호 일 때
    if (account.password.isIncorrectPassword(passwordEncoder, rawPassword)) {
      account.password.addFailedCount()
      accounts.saveAndFlush(account)

      throw LoginFailException()
    }

    // 로그인 성공 시 메타데이터 추가
    account.password.resetFailedCount() // 비밀번호 실패 횟수 초기화
    account.lastLoginAt = now() // 최근 로그인 일시 추가
    account.lastLoginIp = getHttpServletRequest().getClientIp() // 클라이언트 IP 수집

    // 변경 내역 저장 후 리턴
    return accounts.saveAndFlush(account)
  }

  /**
   * 토큰 발급
   *
   * @param account 토큰을 발급할 사용자 계정
   * @return JWT 토큰
   */
  fun issueToken(account: Account): AuthResponse {
    val accessToken = issueAccessTokenAndExpireInstant(account)
    val refreshToken = UUID.randomUUID()

    // 세션 저장
    saveSession(account, accessToken.first, refreshToken)

    // 리턴
    return AuthResponse(
      accessToken = accessToken.first,
      refreshToken = refreshToken,
      expires = accessToken.second
    )
  }

  /**
   * 토큰 재발급
   *
   * @param refreshToken 재발급 대상 UUID
   * @return 재발급된 JWT 토큰
   */
  fun refreshToken(refreshToken: UUID): AuthResponse {
    val session = sessions.findByRefreshToken(refreshToken) ?: throw ExpireTokenException()
    val account = accounts.findByIdOrNull(session.id) ?: throw LoginFailException()

    // 엑세스 토큰 발급
    val accessToken = issueAccessTokenAndExpireInstant(account)

    // 세션 저장
    saveSession(account, accessToken.first, refreshToken)

    return AuthResponse(
      accessToken = accessToken.first,
      refreshToken = refreshToken,
      expires = accessToken.second
    )
  }

  /**
   * 토큰 세션 제거
   */
  @Transactional
  fun revokeToken(account: Account) {
    sessions.deleteById(account.id)
  }

  /**
   * 엑세스 토큰과 함게 유효기간을 함께 리턴
   *
   * @param account 토큰을 발급할 계정
   * @return [Pair.first] 엑세스토큰, [Pair.second]: 유효기간
   */
  fun issueAccessTokenAndExpireInstant(account: Account): Pair<String, Instant> {
    val expires = now().plus(expires)
    val token = issueAccessToken(account, expires)

    return token to expires
  }

  /**
   * 엑세스 토큰 발급
   *
   * @param account 발급 대상 계정
   * @param expireDate 토큰 유효기간
   * @return Jwt 엑세스 토큰
   */
  fun issueAccessToken(account: Account, expireDate: Instant): String {
    return JWT.create()
      .withJWTId(account.id.toString())
      .withClaim("username", account.username)
      .withClaim("roles", account.getRolesToString())
      .withClaim("last_login_at", account.lastLoginAt?.toString())
      .withIssuer(issuer)
      .withIssuedAt(Date())
      .withExpiresAt(Date.from(expireDate))
      .sign(algorithm)
  }

  @Transactional
  fun saveSession(account: Account, accessToken: String, refreshToken: UUID) {
    val session = sessions.findById(account.id)
      ?.apply { this.accessToken = accessToken } // 엑세스 토큰 업데이트
      ?: AuthSession(
        // 토큰이 기존에 존재하지 않으면 새로 생성
        id = account.id,
        accessToken = accessToken,
        refreshToken = refreshToken,
        expires = now().plus(refreshExpires).epochSecond,
      )

    sessions.save(session)
  }

  /**
   * 토큰 유효성 검증
   *
   * @param token 유효성을 검증할 토큰
   * @return 유효한 토큰일 경우 true
   */
  @Transactional
  fun validateToken(token: String?): Boolean {
    if (token == null) {
      return false
    }

    return try {
      verifier.verify(token) != null &&
        sessions.findByAccessToken(token) != null
    } catch (ignore: JWTVerificationException) { // Jwt 유효성 검증 실패 시 발생
      return false
    }
  }

  /**
   * 엑세스 토큰으로 사용자 정보를 가져옴
   */
  @Transactional
  fun getAccountByToken(accessToken: String): Account {
    val id = getAccountIdByToken(accessToken)

    return accounts.findByIdOrNull(id)
      ?: throw LoginFailException()
  }

  /**
   * 토큰을 파싱해서 계정 정보를 가져온다.
   *
   * @param accessToken 파싱할 토큰
   * @return 토큰 사용자의 기본키
   */
  fun getAccountIdByToken(accessToken: String): Long {
    return verifier.verify(accessToken).id.toLong()
  }
}
