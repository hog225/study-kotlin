package org.yg.kotlinspring.sse

import org.springframework.context.ApplicationEvent
import java.util.concurrent.atomic.AtomicBoolean

class ProgressEvent(source: Any?, id: String, progress: Boolean, init: String) : ApplicationEvent(source!!) {
    private val event: Event

    init {
        event = Event(id, AtomicBoolean(progress), init)
    }

    fun getEvent(): Event {
        return this.event
    }

    fun updateProgress() {
        event.progress.set(false)
    }


    data class Event(
        val id: String,
        val progress: AtomicBoolean,
        val progressCount: String
        ) {


        override fun toString(): String {
            return "Event(id='$id', progressCount=$progressCount)"
        }
    }
}
