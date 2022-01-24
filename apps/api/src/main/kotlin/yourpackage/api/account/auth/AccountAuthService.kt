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
package yourpackage.api.account.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import yourpackage.api.account.AccountRepository
import yourpackage.api.account.auth.exception.AccountIsLockedException
import yourpackage.api.account.auth.exception.AccountNotFoundException
import yourpackage.api.account.auth.jwt.JwtService
import yourpackage.api.account.auth.jwt.JwtToken
import java.time.Instant

/**
 * 계정 서비스
 */
@Service
class AccountAuthService(
  private val repo: AccountRepository,
  private val jwts: JwtService,
  private val encoder: PasswordEncoder
) {
  /**
   * 로그인
   *
   * @param auth 사용자의 인증 요청
   * @param requestIp 사용자 IP
   * @return 발급된 토큰 정보
   */
  fun authorize(auth: AuthorizationRequest, requestIp: String): JwtToken {
    val account = repo.findByUsernameAndEnableIsTrue(auth.username!!)
      ?: throw AccountNotFoundException()

    // 계정이 잠금 시도 상태인지 확인
    if (account.password.isLocked()) {
      throw AccountIsLockedException()
    }

    // 비밀번호가 일치하는지 확인
    if (account.password.isIncorrectPassword(encoder, auth.rawPassword!!)) {
      throw AccountNotFoundException()
    }

    // 로그인 성공 시 후처리
    account.password.resetFailedCount() // 비밀번호 입력 실패 횟수 초기화
    account.lastLoginAt = Instant.now()
    account.lastLoginIp = requestIp
    repo.saveAndFlush(account)

    return jwts.issueToken(account)
  }
}
