package org.yg.kotlinspring.sse

import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

/**
 * Server-Sent Events 테스트 코드
 * /do-action/{id} 으로 비동기 이벤트 발생 * Id 별로 나눠서 이벤트 전송 가능
 * /action-progress/{id} Progress 조회 ( id 별로 조회 가능 )
 * 개선사항
 * - Time Out 처리 ( 실제 비지니스 로직이 끝났을 경우 TimeOut 처리, 진행 상황이 없다면 Complete or error 처리 )
 * - Client 에서 SSE 연결시 기본 패킷 전송
 * - 작업 진행 상황일 경우 해당 작업의 결과물을 조회 하려고 하면 작업 진행 상황을 전송 혹은 /action-progress/{id} 로 라우팅 
 * @property applicationEventPublisher ApplicationEventPublisher
 * @property actionService ActionService
 * @property emitters SseEmitters
 * @constructor
 */
@RestController
@RequestMapping("/sse/mvc")
class EventWatchController(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val actionService: ActionService,
): ApplicationListener<ProgressEvent> {
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

    override fun onApplicationEvent(event: ProgressEvent) {
        println(event.getEvent())
        emitters.send(event.getEvent().id, event.getEvent().toString())
    }
}
