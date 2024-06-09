package org.yg.kotlinspring.excel.service

import org.springframework.stereotype.Service
import org.yg.kotlinspring.excel.model.ExcelResult
import org.yg.kotlinspring.excel.model.Scene
import org.yg.kotlinspring.excel.model.ScheduleExcel
import java.io.ByteArrayInputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class MakeExcelService {

    fun getScheduleExcelData(scheduleExcel: ScheduleExcel): ExcelResult {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")
        val formattedString = ZonedDateTime.now().format(formatter)
        val fileName = "schedule_$formattedString.xlsx"

        return ExcelResult(
            fileName,
            ByteArrayInputStream(ScheduleExcelMaker.scheduleToXlsx(scheduleExcel))
        )

    }

}