// (c) 2022 companyname All rights reserved.
package yourpackage.api.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
  fun findByIdAndEnableIsTrue(id: Long): Account?
  fun findByUsernameAndEnableIsTrue(username: String): Account?
}
