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
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * 계정 패스워드 정의
 */
@Embeddable
data class AccountPassword(
  @Column(name = "encrypted_password") // 보안 상 비밀번호 직접 접근은 허용하지 않는다.
  private var encryptedPassword: String,

  @Column(name = "login_failed_count")
  private var failedCount: Short = 0,

  @Column(name = "last_login_failed_at")
  private var lastLoginFailedAt: Instant? = null,
) {

  /**
   * 비밀번호 실패 횟수 초기화
   */
  fun resetFailedCount() {
    this.failedCount = 0
    this.lastLoginFailedAt = null
  }

  /**
   * 비밀번호 변경 메소드
   *
   * @param encoder 암호화 인코더
   * @param rawPassword 평문 비밀번호
   */
  fun changePassword(encoder: PasswordEncoder, rawPassword: String) {
    this.encryptedPassword = encoder.encode(rawPassword)
    this.resetFailedCount()
  }

  /**
   * 비밀번호가 올바르지 않은지 확인
   *
   * @param encoder 비밀번호 인코더
   * @param rawPassword 평문 비밀번호
   * @return 올바르지 않은 비밀번호일 때 true 리턴
   */
  fun isIncorrectPassword(encoder: PasswordEncoder, rawPassword: String): Boolean {
    return !encoder.matches(rawPassword, this.encryptedPassword)
  }

  /**
   * 비밀번호 오류 횟수 초과로 계정 잠금 상태인지 확인하는 로직
   */
  fun isLocked(): Boolean {
    return this.failedCount >= 5 &&
      this.lastLoginFailedAt?.plus(30, ChronoUnit.MINUTES)?.isAfter(Instant.now()) == true
  }

  override fun toString(): String = "${this::class.simpleName} (Password is not displayed)"
}
