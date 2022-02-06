// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.error.exception

import org.springframework.http.HttpStatus

/**
 * `projectname`의 비지니스 예외
 *
 * @param status HTTP 상태코드
 * @param errorCode 에러코드
 * @param e 연관된 예외
 */
open class ProjectnameException(
  open val status: HttpStatus,
  open val errorCode: String? = null,

  open val e: Throwable? = null,
) : RuntimeException("[$status] $errorCode", e)
