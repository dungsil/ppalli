// (c) 2022 companyname All rights reserved.
package yourpackage.api.account.extension

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import yourpackage.api.account.AccountRole

/**
 * Spring security에서 사용하는 [GrantedAuthority] 인터페이스를 반환한다.
 */
fun List<AccountRole>.toAuthorities(): MutableList<GrantedAuthority> {
  return this.map { SimpleGrantedAuthority(it.role) }
    .toMutableList()
}
