package com.yourcompany.common.error

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

/**
 * 오류 컨트롤러
 */
@RestController
@RequestMapping("\${server.error.path:\${error.path:/error}}")
class ErrorController : ErrorController {

  @RequestMapping
  fun handleError(res: HttpServletResponse, e: Exception?): ResponseEntity<ErrorResponse> {
    if (res.status == 200) {
      res.status = 404
    }

    val status = HttpStatus.valueOf(res.status)

    return ResponseEntity
      .status(status)
      .body(ErrorResponse(status.name))
  }
}
