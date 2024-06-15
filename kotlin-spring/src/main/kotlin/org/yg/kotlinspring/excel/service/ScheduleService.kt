package org.yg.kotlinspring.excel.service

import org.springframework.stereotype.Service
import org.yg.kotlinspring.excel.enums.SiteCode
import org.yg.kotlinspring.excel.enums.TimeCode
import org.yg.kotlinspring.excel.model.Project
import org.yg.kotlinspring.excel.model.Scene
import org.yg.kotlinspring.excel.model.ScheduleExcel
import java.util.*
import java.util.stream.IntStream
import kotlin.streams.toList

@Service
class ScheduleService {

    fun getRandomSchedule(round: Int, maxSceneCount: Int): ScheduleExcel {
        return ScheduleExcel(
            Project(
                "Project Name",
                "Production",
                "Scenario",
                "Producer",
            ),
            getRandomScenes(round, maxSceneCount)
        )
    }

    private fun getNextDate(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, 1)
        return cal.time
    }

    fun getRandomScenes(round: Int, maxSceneCount: Int): Map<Date, List<Scene>> {
        var startDate = Date()
        val randomEpisode = Random().nextInt(10) + 1
        val sceneMap = mutableMapOf<Date, List<Scene>>()
        for (r in 1..round) {
            val sMap = IntStream.range(1, maxSceneCount + 1).mapToObj { s ->
                Scene(
                    episode = randomEpisode.toString(),
                    sceneNum = (r + s).toString(),
                    timeslot = TimeCode.values()[Random().nextInt(TimeCode.values().size)],
                    site = SiteCode.values()[Random().nextInt(SiteCode.values().size)],
                    bigPlayPlaces = listOf("bigPlayPlaces"),
                    playPlaces = listOf("playPlaces"),
                    characters = listOf("characters"),
                    subCharacters = listOf("subCharacters"),
                    costumes = listOf("costumes"),
                    props = listOf("props"),
                    makeups = listOf("makeups"),
                    extras = listOf("extras"),
                    etcs = listOf("etcs"),
                    sfxs = listOf("sfxs"),
                    summarize = "summarize",
                    shootingTime = 100L
                )
            }.toList()
            startDate = getNextDate(startDate)
            sceneMap[startDate] = sMap
        }
        return sceneMap
    }
}