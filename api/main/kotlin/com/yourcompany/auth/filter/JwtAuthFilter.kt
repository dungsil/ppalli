package com.yourcompany.auth.filter

import com.yourcompany.auth.AuthService
import com.yourcompany.common.extension.getAccessToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

/**
 * 사용자 요청 헤더에서 엑세스 토큰을 가져와 파싱하는 피러
 */
@Component
class JwtAuthFilter(private val srv: AuthService) : OncePerRequestFilter() {
  @Transactional
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, c: FilterChain) {
    val token = request.getAccessToken()

    if (srv.validateToken(token)) { // 토큰 유효성 검증
      val account = srv.getAccountByToken(token!!)

      val authentication = UsernamePasswordAuthenticationToken(
        account,
        null, // 비밀번호는 포함하지 않음
        account.getAuthorities()
      )

      SecurityContextHolder.getContext().authentication = authentication
    }

    c.doFilter(request, response)
  }
}
