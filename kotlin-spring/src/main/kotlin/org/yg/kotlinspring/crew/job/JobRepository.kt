package org.yg.kotlinspring.crew.job

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface JobRepository : JpaRepository<JobEntity, Long> {

    override fun findById(id: Long): Optional<JobEntity>
}