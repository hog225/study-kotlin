package org.yg.kotlinspring.domain.job

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class JobEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val start: ZonedDateTime = ZonedDateTime.now(),
    val end: ZonedDateTime = ZonedDateTime.now(),
    val position: String = "대리",
    val capacity: Int = 0

) {

}