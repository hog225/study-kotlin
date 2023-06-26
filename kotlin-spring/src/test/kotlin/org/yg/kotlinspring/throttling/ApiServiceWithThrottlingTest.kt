package org.yg.kotlinspring.throttling

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import java.util.stream.Stream


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
    @DisplayName("flux delayElements ListMono ")
    fun callApiWithThrottlingNormalListMono() {
        val results = mutableListOf<Mono<String>>()
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofMinutes(4))) // RPM 제한 설정
            .build();

        val result = Flux.range(0, 10)
            .delayElements(Duration.ofSeconds(1))
            .flatMap {
                Flux.fromIterable(webClientApiService.callApiNormal2())
                    .flatMap { it }
                    .collectList()
                    .doOnNext { println("done" + it) }
            }
            .collectList()
            .block()

        println(result)

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

    @Test
    @DisplayName("CompletableFuture ")
    fun throttlingCompletableFuture() {
        //val executor = Executors.newSingleThreadExecutor()
        val futures: MutableList<CompletableFuture<String>> = ArrayList()
        for (i in 1..100) {
            val future = CompletableFuture.supplyAsync({
                val result = apiService.callApiWait<String>("http://localhost:8000", HttpMethod.GET, null, null)
                result?.body ?: ""
            })
            futures.add(future)
        }

        val str = CompletableFuture.allOf(*futures.toTypedArray()).join()

//        str.thenAcceptAsync({
//            println("result : $it")
//        }).join()



    }

    @Test
    @DisplayName("CompletableFuture Bucket")
    fun throttlingCompletableFutureWithBucket() {
        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(1, Duration.ofSeconds(4))) // RPM 제한 설정
            .build();
        val futures: MutableList<CompletableFuture<String>> = ArrayList()
        for (i in 1..100) {
            val future = CompletableFuture.supplyAsync {
                val result = apiService.callApiWait<String>("http://localhost:8000", HttpMethod.GET, null, null, bucket)
                result?.body ?: ""
            }
            futures.add(future)
        }

        val str = Stream.of(*futures.toTypedArray())
            .parallel()
            .map { obj: CompletableFuture<String> ->
                obj.join()
            }
            .collect(Collectors.toList())
        println(str)
    }

    @Test
    @DisplayName("CompletableFuture Bucket")
    fun throttlingCompletableFutureThenCompose() {

        val bucket = Bucket4j.builder()
            .addLimit(Bandwidth.simple(29, Duration.ofMinutes(1))) // RPM 제한 설정
            .build();
        var future = CompletableFuture.completedFuture<String>(null)

        for (i in 1..30) {
            future = future.thenCompose {
                delay(bucket)
            }.thenApplyAsync { result: String ->
                println("Element: $result")
                result
            }
        }

        future.join()

    }

    private fun delay(bucket: Bucket): CompletableFuture<String>? {
        return CompletableFuture.supplyAsync {
            try {
                val result = apiService.callApiWait<String>("http://localhost:8000", HttpMethod.GET, null, null, bucket)
                result?.body ?: ""
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw RuntimeException(e)
            }

        }
    }

    @Test
    fun completableFuture(){
        val firstTask: CompletableFuture<Void>  = CompletableFuture.runAsync {
            val result = apiService.callApiBasic<String>("http://localhost:8000", HttpMethod.GET, null, null)
        };

        // 두 번째 작업
        val secondTask = firstTask.thenComposeAsync({ result ->
            System.out.println("두 번째 작업 수행");
            CompletableFuture.runAsync {
                val result = apiService.callApiBasic<String>("http://localhost:8000", HttpMethod.GET, null, null)
            }
        }, CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));

        // 결과 대기
        secondTask.join();

    }



}