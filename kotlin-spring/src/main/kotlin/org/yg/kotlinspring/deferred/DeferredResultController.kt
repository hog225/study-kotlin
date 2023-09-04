package org.yg.kotlinspring.deferred

import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult
import java.util.concurrent.ForkJoinPool

/**
 * Long Polling 이라고 생각하면 될듯
 */
@RestController
@RequestMapping("/deferred")
class DeferredResultController {
    @GetMapping("/async")
    fun handleReqDefResult(model: Model?): DeferredResult<ResponseEntity<*>> {
        println("Received async-deferredresult request")
        val output = DeferredResult<ResponseEntity<*>>()
        ForkJoinPool.commonPool().submit {
            println("Processing in separate thread")
            try {
                Thread.sleep(6000)
            } catch (e: InterruptedException) {
            }
            output.setResult(ResponseEntity.ok("ok"))
        }
        println("servlet thread freed")
        return output
    }
}
