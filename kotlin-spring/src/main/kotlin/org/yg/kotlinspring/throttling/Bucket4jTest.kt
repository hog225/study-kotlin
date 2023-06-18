package org.yg.kotlinspring.throttling

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.hibernate.validator.internal.util.Contracts.assertTrue
import java.time.Duration


fun main(args: Array<String>) {
    val refill: Refill = Refill.intervally(10, Duration.ofMinutes(1))
    val limit: Bandwidth = Bandwidth.classic(10, refill)
    val bucket: Bucket = Bucket4j.builder()
        .addLimit(limit)
        .build()

    for (i in 1..10) {
        assertTrue(bucket.tryConsume(1), "true...")
    }

    println(bucket.tryConsume(1))

}
