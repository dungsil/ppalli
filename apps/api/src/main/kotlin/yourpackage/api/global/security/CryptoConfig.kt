// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * 암호화 관련 서비스
 *
 * @param jwtSecret JWT 암호화에 사용되는 비밀 구문
 */
@Configuration
class CryptoConfig {

  /**
   * 비밀번호 암호화 인코더
   */
  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
