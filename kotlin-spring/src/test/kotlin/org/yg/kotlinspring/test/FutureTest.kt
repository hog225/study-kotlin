package org.yg.kotlinspring.test


import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.random.Random


class FutureTest {

    fun sendStripSummaryQuery(str: String): String {
        return str
    }

    suspend fun performTask(): Int {
        println("efefef----")
        return Random.nextInt(100) // 임의의 작업 결과 반환
    }

    @Test
    fun coroutineTest() {
        val jobCount = 100

        runBlocking {
            val jobs = mutableListOf<Deferred<Int>>()

            // 작업 생성 및 비동기 실행
            for (i in 0 until jobCount) {
                val job = GlobalScope.async { performTask() }
                jobs.add(job)
            }

            // 작업 결과 대기 및 처리
            for (job in jobs) {
                val result = job.await()
                delay(1000)
                println("작업 결과: $result")
            }
        }
    }




    @Test
    fun test() {

        val futures = mutableListOf<CompletableFuture<String>>()

        println("eeee111")
        val strs = IntRange(0, 10).map { "----3333 String $it" }
        for (str in strs) {
            val future = CompletableFuture.supplyAsync {
                println(sendStripSummaryQuery((str)))
                str
            }
            futures.add(future)

        }
        println("2434")

        val result = Stream.of(*futures.toTypedArray())
            .map { obj: CompletableFuture<String> ->
//                try {
//                    Thread.sleep(1000) // 1초 대기
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
                val result = obj.get()
                println("result $result")
            }
            .collect(Collectors.toList())





//
//        try {
//            Thread.sleep(4000) // 1초 대기
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        for (future in futures) {
//            try {
//                Thread.sleep(1000) // 1초 대기
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//            println(future.join())
//        }

    }

    @Test
    fun test2() {

        val hello = CompletableFuture.supplyAsync {
            try {
                Thread.sleep(5000) // 1초 대기
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            println("eeee")
            "Hello"
        }
        val mangKyu = CompletableFuture.supplyAsync {
            try {
                Thread.sleep(5000) // 1초 대기
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            println("eeee2")
            "MangKyu"
        }
        val future = hello.thenCompose { mangKyu }
        future.get()

//        val futures = java.util.List.of(hello, mangKyu)
//
//        val result = CompletableFuture.allOf(*futures.toTypedArray<CompletableFuture<*>>())
//            .thenApply(Function<Void, List<String>> { v: Void? ->
//                futures.stream().map { obj: CompletableFuture<String> -> obj.join() }
//                    .collect(Collectors.toList())
//            })
//
//        result.get().forEach(Consumer { x: String? -> println(x) })
    }

    @Test
    fun test4() {

        val hello = CompletableFuture.supplyAsync {
            "Hello"
        }
        val mangKyu = CompletableFuture.supplyAsync {
            "MangKyu"
        }

        val futures = java.util.List.of(hello, mangKyu)

        val result = CompletableFuture.allOf(*futures.toTypedArray<CompletableFuture<*>>())
            .thenApply(Function<Void, List<String>> { v: Void? ->
                futures.stream().map { obj: CompletableFuture<String> -> obj.join() }
                    .collect(Collectors.toList())
            })

        result.get().forEach(Consumer { x: String? -> println(x) })
    }

    @Test
    fun test5() {
        val futures = mutableListOf<CompletableFuture<Void>>()

        for (i in 0 until 100) {
            val future = CompletableFuture.runAsync {
                // 비동기로 실행할 작업
                println("비동기 작업 $i 실행")
            }.thenRunAsync {
                // 이전 작업 실행 후 대기 시간 주기
                try {
                    Thread.sleep(1000) // 1초 대기
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                println("대기 후 작업 $i 실행")
            }

            futures.add(future)
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()
    }

    @Test
    fun test6() {
        val delayed = CompletableFuture.delayedExecutor(3L, TimeUnit.SECONDS)
        CompletableFuture.supplyAsync(Supplier { "someValue" }, delayed)
            .thenAccept(Consumer { x -> println(x) })
            .join()
        CompletableFuture.supplyAsync(Supplier { "someValue" }, delayed)
            .thenAccept(Consumer { x -> println(x) })
            .join()
    }
}