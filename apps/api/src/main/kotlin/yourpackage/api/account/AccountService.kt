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

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import yourpackage.api.account.exception.AccountIsLockedException
import yourpackage.api.account.exception.AccountNotFoundException
import yourpackage.api.global.utils.getClientIp
import java.time.Instant

@Service
class AccountService(
  private val repo: AccountRepository,
  private val encoder: PasswordEncoder
) {

  /**
   * 사용자 아이디를 기반으로 사용자 정보를 가져오는 메소드
   */
  fun get(id: Long): Account {
    return repo.findByIdAndEnableIsTrue(id)
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

    // 사용자 요청을 가져옴
    val req = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

    // 로그인 성공 시 후처리
    account.password.resetFailedCount() // 비밀번호 입력 실패 횟수 초기화
    account.lastLoginAt = Instant.now()
    account.lastLoginIp = req.getClientIp()

    return repo.saveAndFlush(account)
  }
}
