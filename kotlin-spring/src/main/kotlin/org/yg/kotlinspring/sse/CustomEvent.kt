package org.yg.kotlinspring.sse

import org.springframework.context.ApplicationEvent

class CustomEvent(source: Any?, id: String, init: String) : ApplicationEvent(source!!) {
    private val event: Event

    init {
        event = Event(id, init)
    }

    fun getEvent(): Event {
        return this.event
    }


    data class Event(
        val id: String,
        val init: String
        ) {


        override fun toString(): String {
            return "Event(id='$id', init=$init)"
        }
    }
}
