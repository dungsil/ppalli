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

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 폴백 오류 컨트롤러 구현체
 * 모든 예외처리에 걸리지 않는 경우 이 컨트롤러에서 처리된다.
 */
@RestController
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class ErrorControllerImpl(
  @Value("\${server.error.path:\${error.path:/error}}")
  val errorPath: String
) : ErrorController {
  private val log = KotlinLogging.logger {}

  @RequestMapping
  fun handleFallbackError(
    request: HttpServletRequest,
    response: HttpServletResponse,
    e: Exception?
  ): ResponseEntity<Unit> {
    var status = HttpStatus.valueOf(response.status)

    // 에러페이지에서 성공이 표시되지 않도록
    if (request.requestURI == errorPath) {
      status = HttpStatus.NOT_FOUND
    }

    if (e != null) {
      log.debug { e.printStackTrace() }
    }

    return ResponseEntity
      .status(status)
      .build()
  }
}
