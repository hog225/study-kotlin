package org.yg.kotlinspring.throttling

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RequestCallback
import org.springframework.web.client.ResponseExtractor
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException
import java.time.Duration


@Service
class ApiServiceWithThrottling(
    private val restTemplate: RestTemplate,
) {


    fun <T> callApi(
        url: String,
        method: HttpMethod,
        requestCallback: RequestCallback?,
        responseExtractor: ResponseExtractor<ResponseEntity<T>?>?,
        bucket: Bucket
    ): ResponseEntity<T>? {
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        return if (probe.isConsumed) {
            restTemplate.execute(url, method, requestCallback, responseExtractor)
        } else {
            throw RuntimeException("API 호출량이 초과되었습니다.")
        }
    }

    fun <T> callApiWait(
        url: String,
        method: HttpMethod,
        requestCallback: RequestCallback?,
        responseExtractor: ResponseExtractor<ResponseEntity<T>?>?,
        bucket: Bucket
    ): ResponseEntity<T>? {
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        return if (probe.isConsumed) {
            restTemplate.execute(url, method, requestCallback, responseExtractor)
        } else {
            val waitForRefillMillis = probe.nanosToWaitForRefill / 1000000
            try {
                Thread.sleep(waitForRefillMillis)
                restTemplate.execute(url, method, requestCallback, responseExtractor)
            } catch (e: InterruptedException) {
                throw RuntimeException("API 호출량이 초과되었습니다.")
            }
        }
    }

    @Async
    fun <T> callApiWaitAsync(
        url: String,
        method: HttpMethod,
        requestCallback: RequestCallback?,
        responseExtractor: ResponseExtractor<ResponseEntity<T>?>?,
        bucket: Bucket
    ): ResponseEntity<T>? {
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        return if (probe.isConsumed) {
            restTemplate.execute(url, method, requestCallback, responseExtractor)
        } else {
            val waitForRefillMillis = probe.nanosToWaitForRefill / 1000000
            try {
                Thread.sleep(waitForRefillMillis)
                restTemplate.execute(url, method, requestCallback, responseExtractor)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt();
                throw RuntimeException("API 호출량이 초과되었습니다.")
            }
        }
    }

}