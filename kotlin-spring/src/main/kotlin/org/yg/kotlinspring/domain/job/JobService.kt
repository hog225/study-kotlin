package org.yg.kotlinspring.domain.job

import org.yg.kotlinspring.domain.job.dto.JobCreateRequest
import org.yg.kotlinspring.domain.job.dto.JobUpdateRequest

interface JobService {
    fun get(jobId: Long): JobEntity
    fun getAll(jobId: Long): List<JobEntity>
    fun create(request: JobCreateRequest): JobEntity
    fun update(request: JobUpdateRequest): JobEntity
    fun delete(jobId: Long)


}