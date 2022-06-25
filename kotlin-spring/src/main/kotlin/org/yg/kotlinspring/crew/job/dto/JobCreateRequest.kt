package org.yg.kotlinspring.crew.job.dto

import java.time.ZonedDateTime

class JobCreateRequest(
    val start: ZonedDateTime,
    val end: ZonedDateTime,
    val position: String,
    val capacity: Int = 0
) {
}