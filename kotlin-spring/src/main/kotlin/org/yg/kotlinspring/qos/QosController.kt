package org.yg.kotlinspring.qos

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@RestController
@RequestMapping("/study-kotlin/v1")
class QosController(
    private val myRateLimiter: MyRateLimiter,
    private val requestQueueProcessor: RequestQueueProcessor
) {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    @GetMapping("/resource")
    fun limitedResource(): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            if (myRateLimiter.tryConsume()) {
                "This is a rate-limited resource Tye Consume Success\n\r"
            } else {
                println("Queueing request")
                val result = CompletableFuture<String>()
                requestQueueProcessor.queueRequest {
                    result.complete("This is a rate-limited resource\n\r")
                }
                result.join()
            }
        }
    }

    @GetMapping("/resource2")
    fun limitResourceOne(): String {
        println("limitResourceOne")
        if (lock.isLocked)
            return "This is a rate-limited resource\n\r"
        lock.lock()
        Thread.sleep(2000)
        lock.unlock();
        return "hello ? \n\r"

    }

    @GetMapping("/resource3")
    fun limitResourceOne2(): String {
        try {
            if (!lock.tryLock(2000, TimeUnit.MILLISECONDS))
                return "This is a rate-limited resource\n\r"
            Thread.sleep(500)
            return "hello ? \n\r"
        } finally {
            lock.unlock();
        }

    }

}