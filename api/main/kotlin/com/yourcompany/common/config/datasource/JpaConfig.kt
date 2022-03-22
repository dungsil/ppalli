package com.yourcompany.common.config.datasource

import com.yourcompany.PPALLIApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * `spring-boot-data-jdbc` 설정
 */
@Configuration
@EntityScan(basePackageClasses = [PPALLIApplication::class])
@EnableJpaRepositories(basePackageClasses = [PPALLIApplication::class])
@EnableJpaAuditing(modifyOnCreate = false)
class JpaConfig
