package com.yourcompany.common.error

import com.yourcompany.common.exception.PPALLIException
import com.yourcompany.common.error.ErrorResponse.Companion.UNKNOWN
import com.yourcompany.common.error.ErrorResponse.Companion.VALIDATION_ERROR
import com.yourcompany.common.error.ErrorResponse.Error
import com.yourcompany.common.error.ErrorResponse.FieldError
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.UnsupportedMediaTypeStatusException

@RestControllerAdvice
class ErrorControllerAdvice {

  @ExceptionHandler(PPALLIException::class)
  fun handleBusinessException(e: PPALLIException): ResponseEntity<ErrorResponse> {
    return ResponseEntity
      .status(e.status)
      .body(e.error)
  }

  /**
   * [org.springframework.web.bind.annotation.RequestBody] 어노테이션을 사용하는 컨트롤러에 body 없이 요청 시
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ErrorResponse {
    return if (e.message?.contains("request body is missing") == true) {
      ErrorResponse(VALIDATION_ERROR, listOf(Error("EMPTY_BODY")))
    } else {
      handleException(e)
    }
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(BindException::class)
  fun handleBindException(e: BindException): ErrorResponse {
    val errors = mutableListOf<Error>()

    for (fieldError in e.fieldErrors) {
      val codeAndArguments: Pair<String, Map<String, Any>?> = when (fieldError.code) {
        "NotNull" -> "EMPTY_FIELD" to null
        "Length" -> fieldError.let {
          val length = it.rejectedValue?.toString()?.length ?: 0
          val min = (it.arguments?.get(2) as? Int) ?: 0
          val max = (it.arguments?.get(1) as? Int) ?: 0

          return@let when {
            length < min -> "TOO_SHORT_FIELD" to mapOf("min" to min)
            length > max -> "TOO_LONG_FIELD" to mapOf("max" to max)
            else -> UNKNOWN to mapOf("min" to min, "max" to max, "length" to length)
          }
        }
        else -> VALIDATION_ERROR to null
      }

      errors.add(FieldError(codeAndArguments.first, toSnake(fieldError.field), codeAndArguments.second))
    }

    return ErrorResponse("VALIDATION_ERROR", errors)
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MissingRequestCookieException::class)
  fun handleMissingRequestCookie(e: MissingRequestCookieException): ErrorResponse {
    return ErrorResponse(VALIDATION_ERROR, listOf(Error("MISSING_COOKIE", mapOf("name" to e.cookieName))))
  }

  @ResponseStatus(METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
  fun handleHttpRequestMethodNotSupportedException(): ErrorResponse {
    return ErrorResponse(METHOD_NOT_ALLOWED, null)
  }

  @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(UnsupportedMediaTypeStatusException::class)
  fun handleUnsupportedMediaTypeStatusException(): ErrorResponse {
    return ErrorResponse(UNSUPPORTED_MEDIA_TYPE, null)
  }

  /**
   * 예외처리되지 않은 예외
   */
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception::class)
  fun handleException(e: Exception): ErrorResponse {
    e.printStackTrace()

    return ErrorResponse(UNKNOWN)
  }

  private fun toSnake(str: String): String {
    return str.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase()
  }
}
