package org.yg.kotlinspring.excel.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.yg.kotlinspring.throttling.ApiServiceWithThrottling
import org.yg.kotlinspring.throttling.ThrottleConfig
import org.yg.kotlinspring.throttling.WebClientApiServiceWithThrottling
import java.io.FileOutputStream
import kotlin.io.path.createTempDirectory

@SpringBootTest(classes = [
    ScheduleService::class,
])
class ScheduleExcelMakerTest @Autowired constructor(
    private val scheduleService: ScheduleService,
) {

    @Test
    fun scheduleToXlsx() {
        val schedule = scheduleService.getRandomSchedule(4, 6)
        val stream = ScheduleExcelMaker.scheduleToXlsx(schedule)
        assertNotNull(stream)

        try {
            createTempDirectory()
            FileOutputStream("${TestExcelMaker.TMP_DIRECTORY_NAME}/schedule.xlsx").use {
                if (stream != null) {
                    it.write(stream)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}