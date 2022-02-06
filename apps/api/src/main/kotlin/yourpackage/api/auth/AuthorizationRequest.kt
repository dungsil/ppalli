// (c) 2022 companyname All rights reserved.
package yourpackage.api.auth

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

/**
 * 승인요청
 *
 * **NOTE**: Jackson 직렬화를 위해 Kotlin 타입은 nullable 하게 선언하며 검증 어노테이션으로 실제 `not null`을 체크한다.
 */
data class AuthorizationRequest(

  @field:NotNull
  @field:Length(min = 3, max = 100)
  val username: String? = null,

  @field:NotNull
  @field:Length(min = 8)
  val rawPassword: String? = null
)
