package com.yourcompany.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
  fun findByUsernameAndEnableIsTrue(username: String): Account?
}
