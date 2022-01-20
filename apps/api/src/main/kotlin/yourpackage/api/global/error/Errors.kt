/*
 * MIT License
 *
 * Copyright ⓒ 2022 Kim Younggeon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 */
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
