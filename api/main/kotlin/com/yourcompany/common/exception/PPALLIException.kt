package com.yourcompany.common.exception

import com.yourcompany.common.error.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException

/**
 * 공통 비지니스 예외
 *
 * @param status 발생 시 사용자에게 전달할 HTTP 상태코드
 * @param error 사용자에게 표시할 HTTP 오류 메시지
 */
open class PPALLIException(
  val status: HttpStatus,
  val error: ErrorResponse = ErrorResponse(status.name),
  override val cause: Throwable? = null
) : HttpStatusCodeException(
  status,
  buildString {
    append("[")
    append(error.code)
    append("] ")

    if (error.errors != null) {
      error.errors.forEach {
        append(it.code)
        append("(")
        append(it.arguments)
        append(")")
      }
    }
  }
)
