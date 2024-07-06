package org.yg.kotlinspring.qos

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

@Component
class RequestQueueProcessor {
    private val requestQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()

    fun queueRequest(request: Runnable) {
        requestQueue.offer(request)
    }

    @Scheduled(fixedRate = 100)
    fun processQueue() {
        while (requestQueue.isNotEmpty()) {
            val request = requestQueue.poll()
            request?.run()
        }
    }
}