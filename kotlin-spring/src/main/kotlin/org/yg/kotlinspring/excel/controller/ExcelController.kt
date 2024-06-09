package org.yg.kotlinspring.excel.controller

import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.yg.kotlinspring.excel.service.MakeExcelService
import org.yg.kotlinspring.excel.service.ScheduleService
import java.net.URLEncoder

@RestController
@RequestMapping("/study-kotlin/v1")
class ExcelController(
    private val makeExcelService: MakeExcelService,
    private val scheduleService: ScheduleService,
) {


    @GetMapping("/excel", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun getCharactersActorExcel(
        @RequestParam round: Int,
        @RequestParam maxSceneCount: Int,
    ): ResponseEntity<InputStreamResource> {
        val scheduleExcel = scheduleService.getRandomSchedule(round, maxSceneCount)
        val excelData = makeExcelService.getScheduleExcelData(scheduleExcel);
        val resource = InputStreamResource(excelData.inputStream)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attatchment; fileName=" + URLEncoder.encode(excelData.fileName, "UTF-8") + ";"
            )
            .body(resource)
    }


}