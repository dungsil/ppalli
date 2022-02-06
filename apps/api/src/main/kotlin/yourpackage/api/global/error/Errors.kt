// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.error

import org.springframework.http.HttpStatus

/**
 * 오류 응답 객체
 *
 * @param errors 오류 목록
 */
data class Errors(val errors: Iterable<Error>) {
  /**
   * 상세 오류 내용 객체
   *
   * @param code 오류 코드
   */
  open class Error(open val code: String)

  /**
   * 필드 오류
   */
  open class FieldError(override val code: String, val fields: Map<String, List<Any>>) : Error(code)

  companion object {
    /**
     * 오류 객체 생성 클래스
     *
     * @param code 오류 코드
     */
    @JvmStatic
    fun of(code: String): Errors {
      val error = Error(code)
      return Errors(listOf(error))
    }

    /**
     * 오류 객체 생성 클래스
     *
     * @param status HTTP 상태 코드
     */
    @JvmStatic
    fun of(status: HttpStatus): Errors {
      val error = Error(status.name)
      return Errors(listOf(error))
    }
  }
}
