package org.yg.kotlinspring.domain.job

import org.yg.kotlinspring.domain.job.dto.JobCreateRequest
import org.yg.kotlinspring.domain.job.dto.JobUpdateRequest

class JobServiceImpl(
    private val jobRepository: JobRepository
) : JobService{
    override fun get(jobId: Long, func: String): JobEntity {
//        when(func) {
//            ""
//        }
        val sup: ()->JobEntity = { JobEntity(3) }
        return jobRepository.findById(jobId).orElseGet(sup)
    }

    fun get1(jobId: Long): JobEntity {
        return jobRepository.findById(jobId).orElseThrow()
    }

    override fun getAll(jobId: Long): List<JobEntity> {
        TODO("Not yet implemented")
    }

    override fun create(request: JobCreateRequest): JobEntity {
        TODO("Not yet implemented")
    }

    override fun update(request: JobUpdateRequest): JobEntity {
        TODO("Not yet implemented")
    }

    override fun delete(jobId: Long) {
        TODO("Not yet implemented")
    }


}