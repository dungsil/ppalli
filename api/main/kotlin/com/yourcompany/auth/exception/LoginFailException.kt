package com.yourcompany.auth.exception

import com.yourcompany.common.exception.PPALLIException
import com.yourcompany.common.error.ErrorResponse
import org.springframework.http.HttpStatus.UNAUTHORIZED

/**
 * 로그인 실패 예외
 */
class LoginFailException : PPALLIException(
  status = UNAUTHORIZED,
  error = ErrorResponse("LOGIN_FAILED")
)
