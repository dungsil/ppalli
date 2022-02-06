// (c) 2022 companyname All rights reserved.
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
class GlobalErrorController(
  @Value("\${server.error.path:\${error.path:/error}}")
  val errorPath: String
) : ErrorController {
  private val log = KotlinLogging.logger {}

  @RequestMapping
  fun handleFallbackError(
    request: HttpServletRequest,
    response: HttpServletResponse,
    e: Exception?
  ): ResponseEntity<Errors> {
    var status = HttpStatus.valueOf(response.status)

    // 에러페이지에서 성공이 표시되지 않도록
    if (request.requestURI == errorPath) {
      status = HttpStatus.NOT_FOUND
    }

    return ResponseEntity
      .status(status)
      .body(Errors.of(status))
  }
}
