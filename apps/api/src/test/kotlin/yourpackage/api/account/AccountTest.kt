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
package yourpackage.api.account

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import yourpackage.api.global.security.CryptoConfig
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DataJpaTest
@Import(CryptoConfig::class)
internal class AccountTest {

  @Autowired
  lateinit var repo: AccountRepository

  @Autowired
  lateinit var encoder: PasswordEncoder

  @Test
  @DisplayName("사용자 추가")
  fun testAddAccount() {
    val account = Account(
      username = "test",
      rawPassword = "foobar123",
      passwordEncoder = encoder,
      email = "test@example.com"
    )

    repo.saveAndFlush(account)

    assertTrue("ID는 1이상이여야 합니다.") { account.id > 0 }
    assertTrue { account.username == "test" }
    assertTrue { account.email == "test@example.com" }

    // AccountRole 테스트
    assertEquals(account, account.roles[0].account)
    assertEquals("default", account.roles[0].role) // TODO: 기본 계정 권한 변경
  }
}
