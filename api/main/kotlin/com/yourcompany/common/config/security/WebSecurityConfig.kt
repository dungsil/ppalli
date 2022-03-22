package com.yourcompany.common.config.security

import com.yourcompany.auth.filter.JwtAuthFilter
import com.yourcompany.common.error.ErrorEntryPoint
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * `spring-boot-starter-security` - 웹 관련 설정
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
  private val jwtAuthFilter: JwtAuthFilter,
  private val errorEntryPoint: ErrorEntryPoint
) : WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    // @formatter:off

    // 미사용 기능 제거
    http
      .csrf().disable()
      .httpBasic().disable()
      .formLogin().disable()
      .sessionManagement().sessionCreationPolicy(STATELESS)

    // 인증 필터 추가
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

    // 미인증 오류 페이지 추가
    http.exceptionHandling().authenticationEntryPoint(errorEntryPoint)

    // URL 인증 매핑
    http.authorizeRequests()
      .mvcMatchers(POST, "/authorize").anonymous() // 로그인 (토큰 발급)
      .mvcMatchers(POST, "/authorize/refresh").permitAll() // 토큰 재발급
      .anyRequest().fullyAuthenticated()

    // @formatter:on
  }
}
