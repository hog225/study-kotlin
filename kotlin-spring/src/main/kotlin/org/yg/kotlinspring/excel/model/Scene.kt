package org.yg.kotlinspring.excel.model

import org.yg.kotlinspring.excel.enums.SiteCode
import org.yg.kotlinspring.excel.enums.TimeCode


data class Scene(
    val episode: String? = null,
    val sceneNum: String? = null,
    val timeslot: TimeCode? = null,
    val site: SiteCode? = null,
    val bigPlayPlaces: List<String>? = null,
    val playPlaces: List<String>? = null,
    val characters: List<String>? = null,
    val subCharacters: List<String>? = null,
    val costumes: List<String>? = null,
    val props: List<String>? = null,
    val makeups: List<String>? = null,
    val extras: List<String>? = null,
    val etcs: List<String>? = null,
    val sfxs: List<String>? = null,
    val summarize: String? = null,
    val shootingTime: Long? = null,
    ) : Iterable<String> {

    override fun iterator(): Iterator<String> {
        return listOf(
            episode ?: "",
            sceneNum ?: "",
            timeslot?.name ?: "",
            site?.name ?: "",
            bigPlayPlaces?.joinToString(", ") ?: "",
            playPlaces?.joinToString(", ") ?: "",
            characters?.joinToString(", ") ?: "",
            subCharacters?.joinToString(", ") ?: "",
            costumes?.joinToString(", ") ?: "",
            props?.joinToString(", ") ?: "",
            makeups?.joinToString(", ") ?: "",
            extras?.joinToString(", ") ?: "",
            etcs?.joinToString(", ") ?: "",
            sfxs?.joinToString(", ") ?: "",
            summarize ?: "",
            shootingTime?.toString() ?: "",
        ).iterator()
    }

    companion object {
        @JvmStatic
        fun displayColumns(): List<String> {
            return listOf(
                "회차",
                "씬번호",
                "타임코드",
                "촬영장소",
                "대형촬영장소",
                "촬영장소",
                "캐릭터",
                "서브캐릭터",
                "의상",
                "소품",
                "메이크업",
                "엑스트라",
                "기타",
                "SFX",
                "총평",
                "촬영시간",
            )
        }
    }
}