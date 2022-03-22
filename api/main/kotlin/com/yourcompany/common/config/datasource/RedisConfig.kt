package com.yourcompany.common.config.datasource

import com.yourcompany.PPALLIApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@Configuration
@EnableRedisRepositories(basePackageClasses = [PPALLIApplication::class])
class RedisConfig
