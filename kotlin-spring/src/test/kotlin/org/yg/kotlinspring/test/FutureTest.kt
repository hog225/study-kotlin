package org.yg.kotlinspring.test


import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

class FutureTest {

    @Test
    fun test() {

        val futures = mutableListOf<CompletableFuture<Void>>()

        val strs = IntRange(0, 99).map { "----3333 String $it" }
        for (str in strs) {
            val future = CompletableFuture.runAsync {
                println("$str 실행됨")
                str
            }.thenRunAsync {
                try {
                    Thread.sleep(5000) // 1초 대기
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            futures.add(future)
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()


//        val future = CompletableFuture.runAsync {
//            // 비동기로 실행할 작업
//            println("비동기 작업 실행")
//        }.thenRunAsync {
//            // 이전 작업 실행 후 대기 시간 주기
//            try {
//                Thread.sleep(1000) // 1초 대기
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//            println("대기 후 작업 실행")
//        }
//
//        future.join() // 작업 완료까지 대기
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
            try {
                Thread.sleep(5000) // 1초 대기
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            "Hello"
        }
        val mangKyu = CompletableFuture.supplyAsync {
            try {
                Thread.sleep(5000) // 1초 대기
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
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
}