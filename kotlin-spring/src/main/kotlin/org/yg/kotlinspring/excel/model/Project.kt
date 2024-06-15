package org.yg.kotlinspring.excel.model

data class Project(
    val projectName: String,
    val production: String,
    val scenario: String,
    val producer: String
) {

    fun getHeaderAndValue() = listOf(
        "연출" to production,
        "극본" to scenario,
        "프로듀서" to producer
    )

}