package org.yg.kotlinspring.throttling

import io.github.bucket4j.Bucket
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.lang.RuntimeException


@Service
class WebClientApiServiceWithThrottling(
    private val webClient: WebClient,
) {


    fun callApi(
        bucket: Bucket
    ): Mono<String> {
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        return if (probe.isConsumed) {
            webClient.get()
                .retrieve()
                .bodyToMono(String::class.java)
        } else {
            throw RuntimeException("API 호출량이 초과되었습니다.")
        }
    }

    fun callApiWait(
        bucket: Bucket
    ): Mono<String> {
        val probe = bucket.tryConsumeAndReturnRemaining(1)
        return if (probe.isConsumed) {
            webClient.get()
                .retrieve()
                .bodyToMono(String::class.java)
        } else {
            val waitForRefillMillis = probe.nanosToWaitForRefill / 1000000
            try {
                Thread.sleep(waitForRefillMillis)
                webClient.get()
                    .retrieve()
                    .bodyToMono(String::class.java)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt();
                throw RuntimeException("API 호출량이 초과되었습니다.")
            }
        }
    }

    fun callApiNormal(): Mono<String> {
        return webClient.get()
            .retrieve()
            .bodyToMono(String::class.java)

    }


}