package org.yg.kotlinspring.domain.job

import org.springframework.data.jpa.repository.JpaRepository

interface JobRepository : JpaRepository<JobEntity, Long> {

}