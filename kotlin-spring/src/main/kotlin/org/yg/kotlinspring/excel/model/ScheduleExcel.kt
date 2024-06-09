package org.yg.kotlinspring.excel.model

import java.util.Date

class ScheduleExcel(
    val project: Project,
    val sceneShootMap: Map<Date, List<Scene>>
) {

}