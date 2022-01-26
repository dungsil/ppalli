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
package yourpackage.servlet.utils

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import yourpackage.api.global.error.GlobalErrorController
import kotlin.test.assertEquals

class GetClientIpExtensionTest {
  private val mock: MockMvc = MockMvcBuilders
    .standaloneSetup(GlobalErrorController::class)
    .build()

  @Test
  @DisplayName("일반적인 상황에서의 클라이언트 IP 획득")
  fun testRemoteAddr() {
    val req = mock.perform(
      get("/")
        .with {
          it.remoteAddr = "48.132.33.189"
          return@with it
        }
    )

    req.andDo { assertEquals("48.132.33.189", it.request.getClientIp()) }
  }

  @Test
  @DisplayName("프록시 서버: `Proxy-Client-IP`를 통해 아이피를 전달하는 경우 IP 획득")
  fun testProxyClientIP() {
    val req = mock.perform(
      get("/")
        .with {
          it.remoteAddr = "96.115.165.254" // 프록시 서버 IP
          it.addHeader("Proxy-Client-IP", "47.78.66.16")
          return@with it
        }
    )

    req.andDo { assertEquals("47.78.66.16", it.request.getClientIp()) }
  }

  @Test
  @DisplayName("프록시 서버: `X-Forwarded-For`를 통해 아이피를 전달하는 경우 IP 획득")
  fun testXForwardedFor() {
    val req = mock.perform(
      get("/")
        .with {
          it.remoteAddr = "202.43.60.241" // 프록시 서버 IP
          it.addHeader("X-Forwarded-For", "93.32.71.149")
          return@with it
        }
    )

    req.andDo { assertEquals("93.32.71.149", it.request.getClientIp()) }
  }
}
