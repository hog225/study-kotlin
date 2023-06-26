package org.yg.kotlinspring.sse

import org.slf4j.LoggerFactory
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer


class SseEmitters {
    private val emitterMap: ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> = ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>>()
    @JvmOverloads
    fun add(id: String, emitter: SseEmitter = SseEmitter()): SseEmitter {
        if (!emitterMap.containsKey(id)) {
            emitterMap[id] = CopyOnWriteArrayList()
        }
        emitterMap[id]?.add(emitter)

        emitter.onCompletion {
            logger.info("Emitter completed: {} {}", id, emitter)
            emitterMap[id]?.remove(emitter)
        }
        emitter.onTimeout {
            logger.info("Emitter timed out: {} {}", id, emitter)
            emitter.complete()
            emitterMap[id]?.remove(emitter)
        }
        return emitter
    }

    fun send(id: String, obj: Any?) {
        send(id) { emitter: SseEmitter -> emitter.send(obj!!) }
    }

//    fun send(builder: SseEmitter.SseEventBuilder?) {
//        send { emitter: SseEmitter -> emitter.send(builder!!) }
//    }

    private fun send(id: String, consumer: SseEmitterConsumer<SseEmitter>) {
        val failedEmitters: MutableList<SseEmitter> = ArrayList()
        emitterMap[id]?.forEach(Consumer { emitter: SseEmitter ->
            try {
                consumer.accept(emitter)
            } catch (e: Exception) {
                emitter.completeWithError(e)
                failedEmitters.add(emitter)
                logger.error("Emitter failed: {}", emitter, e)
            }
        })
        emitterMap[id]?.removeAll(failedEmitters)
    }

    private fun interface SseEmitterConsumer<T> {
        @Throws(IOException::class)
        fun accept(t: T)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SseEmitters::class.java)
    }
}
