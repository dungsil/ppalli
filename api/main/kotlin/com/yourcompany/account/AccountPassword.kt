package com.yourcompany.account

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
   * 비밀번호 실패횟수 추가
   */
  fun addFailedCount() {
    this.failedCount++
    this.lastLoginFailedAt = Instant.now()
  }

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

  override fun toString(): String {
    return "${this::class.simpleName}(" +
      "password = [mask], " +
      "failedCount = $failedCount, " +
      "lastLoginFailedAt = $lastLoginFailedAt" +
      ")"
  }
}
