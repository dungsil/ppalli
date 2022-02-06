// (c) 2022 companyname All rights reserved.
package yourpackage.validator

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

/**
 * IPv4 / IPv6 유효성 검증 어노테이션
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [IpAddress.Validator::class])
annotation class IpAddress(
  val message: String = "{yourpackage.validatorIpAddress.message}",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<*>> = [],
  val nullable: Boolean = true
) {

  class Validator : ConstraintValidator<IpAddress, String> {
    private val ipV4 = Regex("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!\$)|\$)){4}$")
    private val ipv6 = Regex(
      "^(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|" +
        "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
        "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
        "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
        ":((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|" +
        "::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|" +
        "([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))$"
    )
    private var nullable: Boolean = true

    override fun initialize(constraintAnnotation: IpAddress) {
      this.nullable = constraintAnnotation.nullable
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {

      // Check nullable
      if (value == null) {
        return this.nullable
      }

      // Check IPv4
      if (value.matches(ipV4)) {
        return true
      }

      // Check IPv6
      return value.matches(ipv6)
    }
  }
}
