// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import yourpackage.api.global.error.Errors

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class WebSecurityConfig(
  private val jwtAuthFilter: JwtAuthFilter,
  private val om: ObjectMapper,

  @Value("\${api.web.base}")
  private val webBase: String,
) : WebSecurityConfigurerAdapter() {
  override fun configure(http: HttpSecurity) {
    // JWT 인증을 사용하므로 필요없는 항목 제거
    http
      .csrf().disable()
      .httpBasic().disable()
      .formLogin().disable()
      .sessionManagement().sessionCreationPolicy(STATELESS)

    // 미인증시 401 오류 리턴
    http.exceptionHandling()
      .authenticationEntryPoint { _, res, _ ->
        res.status = 401
        res.contentType = "application/json"
        res.writer.write(om.writeValueAsString(Errors.of(UNAUTHORIZED)))
        res.flushBuffer()
      }

    // JWT 필터 추가
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

    // CORS 설정
    http.cors().configurationSource { CorsConfiguration().apply {
      allowedOrigins = listOf(webBase)
      allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")
    }}

    // 인증 경로
    http.authorizeRequests()
      .mvcMatchers(POST, "/auth").anonymous() // 로그인
      .anyRequest().fullyAuthenticated() // 기본 값은 전체 인증필요
  }
}
