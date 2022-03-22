package com.yourcompany.account

import com.yourcompany.common.entity.AuditEntity
import javax.persistence.*

/**
 * 계정 권한
 *
 * @param account 해당 계정
 * @param role 권한
 */
@Entity
@Table(name = "account_role")
class AccountRole private constructor(
  @Id
  @ManyToOne
  @JoinColumn(name ="account_id")
  var account: Account? = null,

  @Id
  @Column(name = "role")
  val role: String
) : AuditEntity() {
  constructor(role: String) : this(null, role)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AccountRole) return false

    if (account != other.account) return false
    if (role != other.role) return false

    return true
  }

  override fun hashCode(): Int {
    var result = account?.hashCode() ?: 0
    result = 31 * result + role.hashCode()
    return result
  }

  override fun toString(): String = this.role

  companion object {
    /**
     * 기본 계정 권한을 가져오는 메소드
     *
     * @return 기본 권한 목록
     */
    fun default(): MutableList<AccountRole> {
      return mutableListOf(AccountRole(role = "default")) // TODO: 기본 권한 해결
    }
  }
}
