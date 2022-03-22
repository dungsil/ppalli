package com.yourcompany.auth.exception

import com.yourcompany.common.exception.PPALLIException
import com.yourcompany.common.error.ErrorResponse
import org.springframework.http.HttpStatus.UNAUTHORIZED

/**
 * 토큰 기간 만료
 */
class ExpireTokenException : PPALLIException(
  status = UNAUTHORIZED,
  error = ErrorResponse(
    code = "EXPIRE_TOKEN"
  )
)
