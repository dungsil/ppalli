package com.yourcompany.auth

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.util.*
import java.util.concurrent.TimeUnit.MICROSECONDS

/**
 * 사용자 세션
 *
 * @property id 사용자 ID
 * @property accessToken 엑세스 토큰
 * @property refreshToken 리프래시 토큰
 * @property expires 엑세스 토큰 만료기간 ( 유닉스 타임 )
 */
@RedisHash("auth_session")
data class AuthSession(

  @Id
  val id: Long,

  @Indexed
  var accessToken: String,

  @Indexed
  val refreshToken: UUID,

  @TimeToLive(unit = MICROSECONDS)
  val expires: Long
)
