package org.yg.kotlinspring.domain.job.dto

import java.time.ZonedDateTime

class JobUpdateRequest(
    val id: Long,
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val position: String,
    val capacity: Int = 0
) {
}