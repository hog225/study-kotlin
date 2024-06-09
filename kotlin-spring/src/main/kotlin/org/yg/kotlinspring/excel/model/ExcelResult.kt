package org.yg.kotlinspring.excel.model

import java.io.InputStream

data class ExcelResult(
    val fileName: String,
    val inputStream: InputStream,
)