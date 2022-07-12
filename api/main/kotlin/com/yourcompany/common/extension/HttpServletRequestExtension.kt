package com.yourcompany.common.extension

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

/**
 * @return  현재 요청의 [HttpServletRequest]를 가져옴
 */
fun getHttpServletRequest(): HttpServletRequest {
  return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
}

/**
 * 클라이언트의 IP를 가져온다.
 *
 * @return IPv4 혹은 IPv6 문자열
 */
fun HttpServletRequest.getClientIp(): String {
  return getHeaders("X-Forwarded-For")?.toList()?.lastOrNull() // proxy, load balance O
    ?: getHeader("Proxy-Client-IP")
    ?: remoteAddr // proxy, load balance x
}

/**
 * @return 엑세스 토큰을 가져온다, 없을 경우 null
 */
fun HttpServletRequest.getAccessToken(): String? {
  return getHeader("Authorization")
    ?.replaceFirst("Bearer ", "") // 토큰타입 제거
}
