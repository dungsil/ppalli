// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.security.usetdetail

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import yourpackage.api.account.Account

data class UserDetailsImpl(val account: Account) : UserDetails {
  override fun getUsername(): String = account.id.toString()
  override fun getPassword(): String? = null
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return mutableListOf(SimpleGrantedAuthority("ROLE_USER")) // FIXME: 권한부여 변경 필요
  }

  override fun isAccountNonLocked(): Boolean = account.password.isLocked()
  override fun isAccountNonExpired(): Boolean = true
  override fun isCredentialsNonExpired(): Boolean = true
  override fun isEnabled(): Boolean = account.enable
}
