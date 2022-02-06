// (c) 2022 companyname All rights reserved.
package yourpackage.api.auth

import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.bind.annotation.*
import yourpackage.api.account.AccountService
import yourpackage.api.auth.jwt.JwtService
import yourpackage.api.auth.jwt.JwtToken
import yourpackage.api.global.error.exception.ProjectnameException
import yourpackage.servlet.utils.getAccessToken
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/**
 * 계정 인증 컨트롤러
 */
@RestController
@RequestMapping("/auth")
class AuthController(
  private val accounts: AccountService,
  private val jwts: JwtService,
) {

  /**
   * 인증 요청 (로그인)
   */
  @PostMapping
  fun authorize(@Valid @RequestBody auth: AuthorizationRequest, res: HttpServletResponse): JwtToken {
    val account = accounts.authorize(auth.username!!, auth.rawPassword!!) // 로그인
    val token = jwts.issueToken(account) // 토큰 발급

    // 리프래시 토큰을 쿠키에 저장
    val cookie = Cookie("refresh_token", token.refreshToken).apply {
      secure = true
      isHttpOnly = true
      path = "/"
    }
    res.addCookie(cookie)

    return token
  }

  /**
   * 로그아웃
   */
  @DeleteMapping
  @ResponseStatus(NO_CONTENT)
  fun revoke(req: HttpServletRequest) {
    val accessToken = req.getAccessToken()
      ?: throw ProjectnameException(UNAUTHORIZED) // 토큰이 없으면 무시

    jwts.revokeToken(accessToken)
  }
}
