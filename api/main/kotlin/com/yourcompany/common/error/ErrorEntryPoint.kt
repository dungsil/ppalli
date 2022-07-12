package com.yourcompany.common.error

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 미인증 오류 컨트롤러
 */
@Component
class ErrorEntryPoint(private val om: ObjectMapper) : AuthenticationEntryPoint {
  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun commence(
    request: HttpServletRequest?,
    response: HttpServletResponse,
    authException: AuthenticationException?
  ) {
    authException?.let { log.debug("Unauthorized error: ${authException.message}", authException) }

    response.apply {
      status = 401 // Unauthorized
      contentType = "application/json;charset=UTF-8"

      writer.apply {
        write(om.writeValueAsString(ErrorResponse(UNAUTHORIZED.name)))
        flush()
      }
    }
  }
}
