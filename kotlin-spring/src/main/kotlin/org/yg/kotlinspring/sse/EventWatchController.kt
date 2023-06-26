package org.yg.kotlinspring.sse

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct


@RestController
@RequestMapping("/sse/mvc")
class EventWatchController(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val actionService: ActionService,
): ApplicationListener<CustomEvent> {
    private val emitters: SseEmitters = SseEmitters()


    @GetMapping(path = ["/action-progress/{id}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getFolderWatch(
        @PathVariable("id") id: String
    ): SseEmitter? {
        println("getFolderWatch $id")
        return emitters.add(id)
    }

    @PostMapping(path = ["/do-action/{id}"])
    fun startFolderWatch(
        @PathVariable("id") id: String
    ) {
        actionService.action(id)
    }

    override fun onApplicationEvent(event: CustomEvent) {
        println(event.getEvent())
        emitters.send(event.getEvent().id, event.getEvent().toString())
    }
}
