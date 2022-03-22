package com.yourcompany.auth

import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthSessionRepository : org.springframework.data.repository.Repository<AuthSession, Long> {
  fun findById(id: Long): AuthSession?
  fun findByAccessToken(accessToken: String): AuthSession?
  fun findByRefreshToken(refreshToken: UUID): AuthSession?

  fun save(entity: AuthSession)
  fun deleteById(id: Long)
}
