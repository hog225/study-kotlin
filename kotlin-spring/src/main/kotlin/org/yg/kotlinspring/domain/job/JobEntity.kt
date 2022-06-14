package org.yg.kotlinspring.domain.job

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class JobEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val position: String,
    val capacity: Int = 0

) {

}