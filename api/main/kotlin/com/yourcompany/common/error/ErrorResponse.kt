package com.yourcompany.common.error

import org.springframework.http.HttpStatus

/**
 * 오류 응답 데이터
 *
 * @property code 오류 코드
 * @property errors 오류 목록
 */
@Suppress("MemberVisibilityCanBePrivate")
data class ErrorResponse(
  val code: String,
  val errors: Iterable<Error>? = null,
) {
  /**
   * 오류 응답 데이터
   *
   * @param status 오류 코드
   * @param errors 오류 목록
   */
  constructor(status: HttpStatus, errors: Iterable<Error>? = null) : this(status.name, errors)

  companion object {
    /** 검증 오류 */
    const val VALIDATION_ERROR = "VALIDATION_ERROR"

    /** 알수없는 오류 */
    const val UNKNOWN = "UNKNOWN"
  }

  /**
   * 공통 오류 추상 클래스
   *
   * @property code 오류 코드
   */
  open class Error(open val code: String, open val arguments: Map<String, Any>? = null)

  /**
   * 필드 오류
   *
   * @property field 필드 이름
   * @property arguments
   */
  class FieldError(
    override val code: String,
    val field: String,
    override val arguments: Map<String, Any>? = null
  ) : Error(code, arguments)
}
