package org.yg.kotlinspring.throttling

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.core.scheduler.Schedulers.parallel
import java.time.Duration
import kotlin.concurrent.thread

/**
 *
 * @property apiService ApiServiceWithThrottling
 * @constructor
 */
@SpringBootTest(classes = [
    ApiServiceWithThrottling::class,
    ThrottleConfig::class,
    WebClientApiServiceWithThrottling::class
])
internal class ApiServiceWithThrottlingTest @Autowired constructor(
    private val apiService: ApiServiceWithThrottling,
    private val webClientApiService: WebClientApiServiceWithThrottling
) {

    @Test
    @DisplayName("4초 당 1개를 받게 됨으로 실패한다 / 서버는 Request 받았을시 3초 대기 후 응답한다. exception 이 발생한다. ")
    fun callApiWithThrottling() {
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofSeconds(4))) // RPM 제한 설정
            .build();
        for (i in 1..10) {
            println("api call start $i")
            val result = apiService.callApi<Object>("http://localhost:8000", HttpMethod.GET, null, null, bucket)
            println(result)
        }
    }

    @Test
    @DisplayName("위 테스트와 조건은 같으나 호출량이 초과될 경우 호출량이 채워질때까지 기다린다. ")
    fun callApiWithThrottling2() {
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofSeconds(4))) // RPM 제한 설정
            .build();
        for (i in 1..10) {
            println("api call start $i")
            val result = apiService.callApiWait<Object>("http://localhost:8000", HttpMethod.GET, null, null, bucket)
            println(result)
        }
    }

    @Test
    @DisplayName("webclient ")
    fun callWebClientApiWithThrottling() {
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(60, Duration.ofMinutes(1))) // RPM 제한 설정
            .build();
        val strs = mutableListOf<String>()
        for (i in 1..10) {

            webClientApiService.callApi(bucket)
                .subscribe {
                    println("api call start $i")
                    strs.add(it)
                }
        }

        Thread.sleep(60000)


    }

    @Test
    @DisplayName("webclient ")
    fun callWebClientApiWithThrottlingWait() {
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofMinutes(4))) // RPM 제한 설정
            .build();
        val strs = mutableListOf<String>()
        for (i in 1..10) {

            webClientApiService.callApiWait(bucket)
                .subscribe {
                    println("api call start $i")
                    strs.add(it)
                }
        }

        Thread.sleep(60000)


    }

    @Test
    @DisplayName("flux delayElements ")
    fun callApiWithThrottlingNormal() {
        val results = mutableListOf<Mono<String>>()
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofMinutes(4))) // RPM 제한 설정
            .build();

        val result = Flux.range(0, 10)
            .delayElements(Duration.ofSeconds(1))
            .parallel()
            .runOn(Schedulers.boundedElastic())
            .flatMap {
                webClientApiService.callApiNormal()
            }



        result.subscribe { println(it) }


        Thread.sleep(60000)
    }

    @Test
    @DisplayName("async ")
    fun callApiWithThrottlingAsync() {
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofSeconds(4))) // RPM 제한 설정
            .build();
        for (i in 1..10) {
            println("api call start $i")
            val result = apiService.callApiWaitAsync<Object>("http://localhost:8000", HttpMethod.GET, null, null, bucket)

        }

        Thread.sleep(60000)
    }



}