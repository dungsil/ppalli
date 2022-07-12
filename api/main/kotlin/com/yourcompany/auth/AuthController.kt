package com.yourcompany.auth

import com.yourcompany.account.Account
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/auth/token")
class AuthController(private val srv: AuthService) {

  /**
   * 회원 인증 ( 로그인 )
   */
  @PostMapping
  @PreAuthorize("hasRole('ROLE_ANONYMOUS')")
  fun authorize(@Valid @RequestBody req: AuthRequest): AuthResponse {
    val account = srv.login(req.username, req.password)
    return srv.issueToken(account)
  }

  /**
   * 토큰 재발급
   */
  @PostMapping("/refresh")
  @Secured("permitAll")
  fun refresh(@CookieValue("refresh_token") refreshToken: UUID): AuthResponse {
    return srv.refreshToken(refreshToken)
  }

  /**
   * 토큰 취소
   */
  @ResponseStatus(NO_CONTENT)
  @DeleteMapping
  fun revoke(authentication: Authentication) {
    val account = authentication.principal as Account
    return srv.revokeToken(account)
  }
}
