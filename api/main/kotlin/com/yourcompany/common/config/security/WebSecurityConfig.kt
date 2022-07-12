package com.yourcompany.common.config.security

import com.yourcompany.auth.filter.JwtAuthFilter
import com.yourcompany.common.error.ErrorEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * `spring-boot-starter-security` - 웹 관련 설정
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class WebSecurityConfig {

  @Bean
  fun securityFilter(http: HttpSecurity, authFilter: JwtAuthFilter, entryPoint: ErrorEntryPoint): SecurityFilterChain {
    return http
      .csrf().disable() // JWT 인증은 CSRF 방지를 하지 않음
      .httpBasic().disable() // JWT 인증만 사용하므로 BASIC 인증 비활성화
      .formLogin().disable() // JWT 인증만 사용하므로 BASIC 인증 비활성화
      .sessionManagement { it.sessionCreationPolicy(STATELESS) } // JWT인증은 세션을 저장하지 않음
      .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT 인증 필터 추가
      .exceptionHandling { it.authenticationEntryPoint(entryPoint) } // 인증 오류 처리 추가
      .authorizeRequests {
        // 기본 URL 인증 정책
        it.anyRequest().fullyAuthenticated() // 기본적으로 전체인증이 필요함
      }
      .build()
  }
}
