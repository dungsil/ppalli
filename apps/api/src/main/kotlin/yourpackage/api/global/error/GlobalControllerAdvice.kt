// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.error

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import yourpackage.api.global.error.Errors.Error
import yourpackage.api.global.error.Errors.FieldError
import yourpackage.api.global.error.exception.ProjectnameException
import org.springframework.validation.FieldError as SpringFieldError

/**
 * 공통 사용자 예외처리 컨트롤러
 */
@RestControllerAdvice
class GlobalControllerAdvice {

  /**
   * `spring-boot-starter-validation`에서 유효성 검증 실패 시
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(BindException::class)
  fun handleBindException(e: BindException): Errors {
    val errors = mutableListOf<Error>()

    val errorContents = mutableMapOf<String, MutableList<SpringFieldError>>()
    for (error in e.bindingResult.fieldErrors) {
      errorContents.computeIfAbsent(error.code!!) { mutableListOf() } // 키 값이 존재하지 않을 때 리스트 생성
      errorContents[error.code]!!.add(error)
    }

    for (content in errorContents) {
      val error = when (content.key) {
        "NotNull" -> FieldError("EMPTY_FIELDS", content.value.associate { it.field to emptyList() })
        "Length" -> FieldError(
          "WRONG_LENGTH",
          content.value.associate { it.field to it.arguments!!.slice(1..2) } // 첫번째는 필요없어서 제거
        )
        else -> FieldError(content.key, content.value.associate { it.field to emptyList() })
      }

      errors.add(error)
    }

    return Errors(errors)
  }

  /**
   * [org.springframework.web.bind.annotation.RequestBody] 어노테이션을 사용하는 컨트롤러에 body 없이 요청 시
   */
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): Errors {
    return if (e.message?.contains("request body is missing") == true) {
      Errors.of("EMPTY_BODY")
    } else {
      handleException(e)
    }
  }

  /**
   * 비지니스 예외 처리
   */
  @ExceptionHandler(ProjectnameException::class)
  fun handleProjectnameException(e: ProjectnameException): ResponseEntity<Errors> {

    return ResponseEntity
      .status(e.status)
      .body(e.errorCode?.let { Errors.of(it) })
  }

  /**
   * 예외처리되지 않은 예외
   */
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception::class)
  fun handleException(e: Exception): Errors {
    e.printStackTrace()

    return Errors.of("UNKNOWN")
  }
}
