package org.yg.kotlinspring.excel.service

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class DateTest {

    @Test
    fun getDateStringTest() {
        val localDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일", Locale.KOREAN)
        println(localDateTime.toLocalDate().format(formatter))
    }

    @Test
    fun getTimeString() {
        val shootTimeSeconds = 345L
        val hours = shootTimeSeconds / 3600
        val minutes = (shootTimeSeconds % 3600) / 60
        val seconds = shootTimeSeconds % 60
        println(String.format("%02d시간 %02d분 %02d초", hours, minutes, seconds))
    }
}