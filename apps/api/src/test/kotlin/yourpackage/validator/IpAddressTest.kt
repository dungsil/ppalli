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
package yourpackage.validator

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IpAddressTest {
  private val validator = IpAddress.Validator()

  @Test
  @DisplayName("유효한 IPv4 주소")
  fun isValidIpv4() {
    assertTrue { validator.isValid("127.0.0.1", null) }
    assertTrue { validator.isValid("0.0.0.0", null) }
    assertTrue { validator.isValid("172.28.110.72", null) }
    assertTrue { validator.isValid("61.58.7.252", null) }
    assertTrue { validator.isValid("227.46.220.95", null) }
    assertTrue { validator.isValid("71.131.254.59", null) }
    assertTrue { validator.isValid("172.63.238.140", null) }
    assertTrue { validator.isValid("24.229.183.15", null) }
    assertTrue { validator.isValid("93.201.161.204", null) }
    assertTrue { validator.isValid("249.120.60.59", null) }
    assertTrue { validator.isValid("103.224.79.246", null) }
    assertTrue { validator.isValid("22.63.212.208", null) }
  }

  @Test
  @DisplayName("유효하지 않은 IPv4 주소")
  fun isInvalidIpv4() {
    assertFalse { validator.isValid("1.0.1.01", null) }
    assertFalse { validator.isValid("256.256.256.256", null) }
    assertFalse { validator.isValid("1.2.3", null) }
  }

  @Test
  @DisplayName("유효한 IPv6 주소")
  fun isValidIpV6() {
    assertTrue { validator.isValid("0:0:0:0:0:0:0:1", null) }
    assertTrue { validator.isValid("b649:5cf5:9cba:da0c:f459:8774:1542:8435", null) }
    assertTrue { validator.isValid("a488:4c81:6de6:d480:59b7:da28:8281:36da", null) }
    assertTrue { validator.isValid("2dfe:1bbf:fd44:afb0:65a9:e024:82db:6206", null) }
    assertTrue { validator.isValid("368e:abec:12c3:8040:d375:cef4:af92:bae4", null) }
    assertTrue { validator.isValid("bd22:9725:f4d6:d0c7:3309:a12a:7db1:1970", null) }
    assertTrue { validator.isValid("5060:d5e9:487b:fdbe:179a:3ca5:0b9a:03bf", null) }
    assertTrue { validator.isValid("7afd:7b3e:7f39:6b2b:5245:c4b1:f653:6652", null) }
    assertTrue { validator.isValid("586d:4c3c:c460:e7a6:46d0:8b93:bf93:18a6", null) }
    assertTrue { validator.isValid("f742:505e:1253:af4b:9aae:f5b7:5a52:a513", null) }
    assertTrue { validator.isValid("cbc1:c490:d52b:8a88:6da7:8319:25c9:dc72", null) }
  }

  @Test
  @DisplayName("유효하지 않은 IPv6 주소")
  fun isInvalidIpv6() {
    assertFalse { validator.isValid("test:test:test:test:test:test:test:test", null) }
  }
}
