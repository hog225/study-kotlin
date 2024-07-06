package org.yg.kotlinspring.qos

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class MyRateLimiter() {
    private lateinit var bucket: Bucket
    @PostConstruct
    fun init() {
        val limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofSeconds(30)))
        bucket = Bucket4j.builder().addLimit(limit).build()
    }

    fun tryConsume(): Boolean {
        return bucket.tryConsume(1)
    }
}