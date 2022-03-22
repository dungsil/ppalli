package com.yourcompany.auth.exception

import com.yourcompany.common.exception.PPALLIException
import com.yourcompany.common.error.ErrorResponse
import org.springframework.http.HttpStatus.UNAUTHORIZED

/**
 * 계정 잠김 예외
 */
class LockedAccountException : PPALLIException(
  status = UNAUTHORIZED,
  error = ErrorResponse("LOCKED_ACCOUNT")
)
