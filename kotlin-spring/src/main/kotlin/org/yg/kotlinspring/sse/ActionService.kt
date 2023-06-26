package org.yg.kotlinspring.sse

import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
class ActionService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Async
    fun action(id: String) {
        val counter = AtomicInteger(0)
        for (i in 0..60) {
            val data = counter.incrementAndGet()
            applicationEventPublisher.publishEvent(CustomEvent(this, id, data.toString()))
            Thread.sleep(1000)
        }

    }
}