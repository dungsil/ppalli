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

import yourpackage.api.global.datasource.CommonEntity
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
  @JoinColumn(name = "account_id")
  var account: Account? = null,

  @Id
  @Column(name = "role")
  val role: String
) : CommonEntity() {
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
