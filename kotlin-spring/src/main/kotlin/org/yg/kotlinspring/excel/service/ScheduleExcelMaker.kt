package org.yg.kotlinspring.excel.service

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.springframework.util.ObjectUtils
import org.yg.kotlinspring.excel.model.ExcelRange
import org.yg.kotlinspring.excel.model.Project
import org.yg.kotlinspring.excel.model.Scene
import org.yg.kotlinspring.excel.model.ScheduleExcel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


object ScheduleExcelMaker {
    private val EXCEL_HEADER_COLOR = IndexedColors.GREY_25_PERCENT;
    fun getDateRange(startDt: LocalDate, endDt: LocalDate): List<LocalDate> {
        val dates = ArrayList<LocalDate>();
        var tmpDate = startDt
        while (tmpDate <= endDt) {
            dates.add(tmpDate)
            tmpDate = tmpDate.plusDays(1)
        }
        return dates

    }

    private fun getDateString(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREAN)
        return dateFormat.format(date)
    }



    private fun writeProject(sheet: SXSSFSheet, project: Project, startRow: Int, colRange: ExcelRange): Int {
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, colRange.start, colRange.start + 1))
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, colRange.start + 2, colRange.start + 4))

        sheet.addMergedRegion(CellRangeAddress(startRow + 1, startRow + 1, colRange.start, colRange.start + 1))
        sheet.addMergedRegion(CellRangeAddress(startRow + 1, startRow + 1, colRange.start + 2, colRange.start + 4))

        sheet.addMergedRegion(CellRangeAddress(startRow + 2, startRow + 2, colRange.start, colRange.start + 1))
        sheet.addMergedRegion(CellRangeAddress(startRow + 2, startRow + 2, colRange.start + 2, colRange.start + 4))

        sheet.addMergedRegion(CellRangeAddress(startRow, startRow + 2, colRange.start + 5, colRange.end))

        val row: Row = sheet.createRow(startRow)
        val cell = row.createCell(colRange.start)

        cell.setCellValue("프로젝트명")

        return 3
    }

    private fun writeRound(sheet: SXSSFSheet, date: Date, round: Int, headerStyle: CellStyle, startRow: Int, colRange: ExcelRange): Int {
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, colRange.start, colRange.end))
        val row: Row = sheet.createRow(startRow)
        val cell = row.createCell(colRange.start)
        cell.cellStyle = headerStyle
        cell.setCellValue("${getDateString(date)} $round 회차")
        return startRow + 1
    }

    private fun writeScenes(sheet: SXSSFSheet, scenes: List<Scene>, headerList: Array<String>, headerStyle: CellStyle, startRow: Int): Int {
        var i = startRow
        if (!ObjectUtils.isEmpty(headerList)) {
            val row = sheet.createRow(i)
            for ((j, header) in headerList.withIndex()) {
                val cell = row.createCell(j)
                cell.setCellValue(header)
                cell.cellStyle = headerStyle
            }
            i++
        }
        for (rowElement in scenes) {
            val row = sheet.createRow(i)
            for ((j, cellElement) in rowElement!!.withIndex()) {
                val cell = row.createCell(j)
                cell.setCellValue(cellElement)
            }
            i++
        }
        return i
    }

    fun scheduleToXlsx(scheduleExcel: ScheduleExcel): ByteArray? {
        ByteArrayOutputStream().use { excelStream ->
            val workbook = SXSSFWorkbook(10000)
            workbook.isCompressTempFiles = true
            val sheet: SXSSFSheet = workbook.createSheet()
            val headerStyle = headerCellStyle(workbook)

            var startRow = 5
            for ((date, scenes) in scheduleExcel.sceneShootMap) {
                val headerList = Scene.displayColumns().toTypedArray()
                val roundNextRow = writeRound(sheet, date, 1, headerStyle, startRow, ExcelRange(0, headerList.size - 1))
                val sceneNextRow = writeScenes(sheet, scenes, headerList, headerStyle, roundNextRow)
                startRow = sceneNextRow + 1
            }

//            if (!ObjectUtils.isEmpty(headerList)) {
//                sheet.setAutoFilter(CellRangeAddress(0, 0, 0, headerList.size - 1))
//            }
            workbook.write(excelStream)
            return excelStream.toByteArray()
        }
    }

    private fun headerCellStyle(workbook: SXSSFWorkbook): CellStyle {
        val headerStyle = workbook.createCellStyle() as XSSFCellStyle
        val headerFont = workbook.createFont()
        headerFont.bold = true

        headerStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
        headerStyle.borderBottom = BorderStyle.THIN
        headerStyle.alignment = HorizontalAlignment.CENTER
        headerStyle.verticalAlignment = VerticalAlignment.CENTER
        return headerStyle
    }

    fun convertToSnakeCase(input: String): String {
        return buildString {
            input.forEachIndexed { index, char ->
                if (char.isUpperCase() && index > 0) {
                    append('_')
                }
                append(char.toLowerCase())
            }
        }
    }
}
