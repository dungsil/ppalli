// (c) 2022 companyname All rights reserved.
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
