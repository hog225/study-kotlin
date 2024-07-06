package org.yg.kotlinspring.qos


import lombok.extern.log4j.Log4j2
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Log4j2
@RestController
@RequestMapping("/study-kotlin/v1")
class QosController(
    private val myRateLimiter: MyRateLimiter,
    private val requestQueueProcessor: RequestQueueProcessor
) {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private val logger = KotlinLogging.logger {}

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
        try {
            lock.lock()
            logger.info { "lock acquired" }
            val random = (Math.random() * 1000).toLong()
            Thread.sleep(random)
            logger.info("finished: $random")
            return "hello ? \n\r"
        } finally {
            lock.unlock();
        }
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