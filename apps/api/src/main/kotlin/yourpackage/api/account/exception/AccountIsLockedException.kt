// (c) 2022 companyname All rights reserved.
package yourpackage.api.account.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import yourpackage.api.global.error.exception.ProjectnameException

class AccountIsLockedException : ProjectnameException(UNAUTHORIZED, "ACCOUNT_LOCKED")
