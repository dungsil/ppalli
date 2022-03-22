package com.yourcompany.account

import com.yourcompany.common.entity.AuditEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import javax.persistence.*
import javax.persistence.FetchType.EAGER
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
class Account private constructor(
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

  @OneToMany(mappedBy = "account", fetch = EAGER)
  val roles: MutableList<AccountRole> = AccountRole.default(),

  @Column(name = "last_login_at")
  var lastLoginAt: Instant? = null,

  @Column(name = "last_login_ip")
  var lastLoginIp: String? = null,

  override var enable: Boolean = true
) : AuditEntity(enable = enable) {

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

  /**
   * @return Spring security 내부에서 사용되는 권한 형식으로 변환하여 반환
   */
  fun getAuthorities(): MutableList<GrantedAuthority> {
    return this.roles
      .map { SimpleGrantedAuthority(it.role) }
      .toMutableList()
  }

  /**
   * @return [roles]의 role 만 문자열 형식으로 반환한다.
   */
  fun getRolesToString(): List<String> {
    return this.roles.map { it.role }
  }


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
