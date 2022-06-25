package org.yg.kotlinspring.crew.job

import org.yg.kotlinspring.crew.job.dto.JobCreateRequest
import org.yg.kotlinspring.crew.job.dto.JobUpdateRequest

interface JobService {
    fun get(jobId: Long, func: String): JobEntity
    fun getAll(jobId: Long): List<JobEntity>
    fun create(request: JobCreateRequest): JobEntity
    fun update(request: JobUpdateRequest): JobEntity
    fun delete(jobId: Long)


}