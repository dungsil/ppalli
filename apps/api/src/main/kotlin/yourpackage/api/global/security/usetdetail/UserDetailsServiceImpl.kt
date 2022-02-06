// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.security.usetdetail

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import yourpackage.api.account.AccountRepository

@Service
class UserDetailsServiceImpl(private val repo: AccountRepository) : UserDetailsService {
  override fun loadUserByUsername(username: String?): UserDetailsImpl? {
    return username
      ?.let { repo.findById(it.toLong()).get() }
      ?.let { UserDetailsImpl(it) }
  }
}
