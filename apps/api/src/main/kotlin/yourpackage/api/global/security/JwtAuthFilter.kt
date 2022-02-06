// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import yourpackage.api.account.AccountService
import yourpackage.api.account.extension.toAuthorities
import yourpackage.api.auth.jwt.JwtService
import yourpackage.servlet.utils.getAccessToken
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthFilter(private val jwts: JwtService, private val accounts: AccountService) : OncePerRequestFilter() {
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, c: FilterChain) {
    val token = request.getAccessToken()

    if (jwts.validateToken(token)) { // 토큰 유효성 검증
      val id = jwts.getAccountIdByToken(token!!)
      val account = accounts.get(id)

      val authentication = UsernamePasswordAuthenticationToken(
        account,
        null,
        account.roles.toAuthorities()
      )

      SecurityContextHolder.getContext().authentication = authentication
    }

    c.doFilter(request, response)
  }
}
