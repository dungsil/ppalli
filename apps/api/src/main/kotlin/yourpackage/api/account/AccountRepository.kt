// (c) 2022 companyname All rights reserved.
package yourpackage.api.account

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AccountRepository : JpaRepository<Account, Long> {
  fun findByIdAndEnableIsTrue(id: Long): Account?
  fun findByUsernameAndEnableIsTrue(username: String): Account?
  fun findByRefreshTokenAndEnableIsTrue(refreshToken: UUID): Account?
}
