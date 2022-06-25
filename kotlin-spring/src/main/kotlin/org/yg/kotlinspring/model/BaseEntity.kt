package org.yg.kotlinspring.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Temporal
import javax.persistence.TemporalType

@MappedSuperclass
open class BaseEntity : Serializable {

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    open var createdAt: ZonedDateTime? = ZonedDateTime.now()

    @LastModifiedDate
    open var updatedAt: ZonedDateTime? = ZonedDateTime.now()

}
