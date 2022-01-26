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
import yourpackage.api.global.datasource.CommonEntity
import yourpackage.validator.IpAddress
import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Email

/**
 * 사용자 계정 엔티티
 */
@Entity
@Table(name = "account")
@SequenceGenerator(
  name = "account_seq",
  initialValue = 1,
  allocationSize = 1
)
data class Account private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
  @Column(name = "account_id")
  val id: Long = 0,

  @Column(name = "username")
  val username: String,

  @Embedded
  val password: AccountPassword,

  @field:Email
  @Column(name = "email")
  var email: String,

  @OneToMany(mappedBy = "account")
  val roles: MutableList<AccountRole> = AccountRole.default(),

  @Column(name = "last_login_at")
  var lastLoginAt: Instant? = null,

  @field:IpAddress
  @Column(name = "last_login_ip")
  var lastLoginIp: String? = null,

  override var enable: Boolean = true
) : CommonEntity(enable = enable) {

  /**
   * 사용자 계정 엔티티
   *
   * @param username 사용자 계정
   * @param rawPassword 비밀번호 평문
   * @param passwordEncoder 비밀번호 암호화 도구
   * @param email 사용자 이메일
   */
  constructor(
    username: String,
    rawPassword: String,
    passwordEncoder: PasswordEncoder,
    email: String,
    roles: MutableList<AccountRole> = AccountRole.default()
  ) : this(
    username = username,
    password = AccountPassword(passwordEncoder.encode(rawPassword)),
    email = email,
    roles = roles
  )

  @PrePersist
  @PreUpdate
  private fun setAccountToRole() {
    for (role in this.roles) {
      role.account = this
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Account) return false

    if (id != other.id) return false
    if (username != other.username) return false
    if (password != other.password) return false
    if (email != other.email) return false
    if (lastLoginAt != other.lastLoginAt) return false
    if (lastLoginIp != other.lastLoginIp) return false
    if (enable != other.enable) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + username.hashCode()
    result = 31 * result + password.hashCode()
    result = 31 * result + email.hashCode()
    result = 31 * result + (lastLoginAt?.hashCode() ?: 0)
    result = 31 * result + (lastLoginIp?.hashCode() ?: 0)
    result = 31 * result + enable.hashCode()
    return result
  }

  // 보안 상 비밀번호를 표시하지 않는다.
  override fun toString(): String = "${this::class.simpleName} (id = $id, username = $username, roles = $roles)"
}
