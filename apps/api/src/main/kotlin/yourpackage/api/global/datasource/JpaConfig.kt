// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.datasource

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import yourpackage.api.ProjectnameApi

@Configuration
@EntityScan(basePackageClasses = [ProjectnameApi::class])
@EnableJpaRepositories(basePackageClasses = [ProjectnameApi::class])
@EnableJpaAuditing(
  modifyOnCreate = false // 최근 수정일 기본 값을 `null`으로
)
class JpaConfig
