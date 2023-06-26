package org.yg.kotlinspring.sse

import org.springframework.context.ApplicationEvent

class ProgressEvent(source: Any?, id: String, init: String) : ApplicationEvent(source!!) {
    private val event: Event

    init {
        event = Event(id, init)
    }

    fun getEvent(): Event {
        return this.event
    }


    data class Event(
        val id: String,
        val progressCount: String
        ) {


        override fun toString(): String {
            return "Event(id='$id', progressCount=$progressCount)"
        }
    }
}
