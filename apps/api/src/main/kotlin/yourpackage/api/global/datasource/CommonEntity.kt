// (c) 2022 companyname All rights reserved.
package yourpackage.api.global.datasource

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.Instant
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * 공통 엔티티
 *
 * @property createdAt 생성일
 * @property lastModifiedAt 최근수정일
 * @property enable 활성화 여부
 */
@MappedSuperclass
abstract class CommonEntity(

  @CreatedDate
  @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
  open val createdAt: Instant = Instant.now(),

  @LastModifiedDate
  @Column(name = "last_modified_at", nullable = true)
  open val lastModifiedAt: Instant? = null,

  @Column(name = "enable", nullable = false)
  open var enable: Boolean = true,
) : Serializable
