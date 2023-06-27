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
    fun action(id: String, event: ProgressEvent) {
        val counter = AtomicInteger(0)
        for (i in 0..5) {
            val data = counter.incrementAndGet()
            applicationEventPublisher.publishEvent(event)
            Thread.sleep(1000)
        }
        event.updateProgress()
        applicationEventPublisher.publishEvent(event)

    }
}