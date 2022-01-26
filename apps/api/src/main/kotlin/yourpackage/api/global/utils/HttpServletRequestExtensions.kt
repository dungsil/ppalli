/*
 * MIT License
 *
 * Copyright ⓒ 2022 Kim Younggeon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 */
package yourpackage.api.global.utils

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
  return getHeader("X-Forwarded-For") // proxy, load balance O
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
