// (c) 2022 companyname All rights reserved.
package yourpackage.api.account.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import yourpackage.api.global.error.exception.ProjectnameException

/**
 * 사용자 계정을 찾을 수 없음
 */
class AccountNotFoundException : ProjectnameException(UNAUTHORIZED, "LOGIN_FAILED")
