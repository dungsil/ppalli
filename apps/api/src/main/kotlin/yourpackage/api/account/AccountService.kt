// (c) 2022 companyname All rights reserved.
package yourpackage.api.account

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import yourpackage.api.account.exception.AccountIsLockedException
import yourpackage.api.account.exception.AccountNotFoundException
import yourpackage.servlet.utils.getClientIp
import yourpackage.servlet.utils.getHttpServletRequest
import java.time.Instant
import java.util.UUID

@Service
class AccountService(
  private val repo: AccountRepository,
  private val encoder: PasswordEncoder
) {

  /**
   * 사용자 아이디를 기반으로 사용자 정보를 가져오는 메소드
   */
  fun getAccountById(id: Long): Account {
    return repo.findByIdAndEnableIsTrue(id)
      ?: throw AccountNotFoundException()
  }

  /**
   * 리프래시 토큰을 기반으로 사용자 정보를 가져온다.
   */
  fun getAccountByRefreshToken(refreshToken: UUID): Account {
    return repo.findByRefreshTokenAndEnableIsTrue(refreshToken)
      ?: throw AccountNotFoundException()
  }

  /**
   * 로그인
   *
   * @param username 사용자 ID
   * @param rawPassword 평문 비밀번호
   * @return 로그인 된 사용자
   */
  fun authorize(username: String, rawPassword: String): Account {
    val account = repo.findByUsernameAndEnableIsTrue(username)
      ?: throw AccountNotFoundException()

    // 계정이 잠금 시도 상태인지 확인
    if (account.password.isLocked()) {
      throw AccountIsLockedException()
    }

    // 비밀번호가 일치하는지 확인
    if (account.password.isIncorrectPassword(encoder, rawPassword)) {
      throw AccountNotFoundException()
    }

    // 로그인 성공 시 후처리
    account.password.resetFailedCount() // 비밀번호 입력 실패 횟수 초기화
    account.lastLoginAt = Instant.now()
    account.lastLoginIp = getHttpServletRequest().getClientIp()

    return repo.saveAndFlush(account)
  }
}
