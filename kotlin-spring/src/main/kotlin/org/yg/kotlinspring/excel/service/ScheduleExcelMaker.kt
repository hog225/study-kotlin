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
import org.yg.kotlinspring.excel.service.ExcellStyleHelper.fontAlignCellStyle
import org.yg.kotlinspring.excel.service.ExcellStyleHelper.headerCellStyle
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


    private fun writeProject(workbook: Workbook, sheet: SXSSFSheet, project: Project, startRow: Int, colRange: ExcelRange): Int {
        val firstColRange = ExcelRange(colRange.start, colRange.start + 1)
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, firstColRange.start, firstColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 1, startRow + 1, firstColRange.start, firstColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 2, startRow + 2, firstColRange.start, firstColRange.end))

        val secondColRange = ExcelRange(colRange.start + 2, colRange.start + 4)
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, secondColRange.start, secondColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 1, startRow + 1, secondColRange.start, secondColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 2, startRow + 2, secondColRange.start, secondColRange.end))

        val cellRowRange = ExcelRange(startRow, startRow + 2)
        val lastColRange = ExcelRange(colRange.start + 5, colRange.end)
        sheet.addMergedRegion(CellRangeAddress(cellRowRange.start, cellRowRange.end, lastColRange.start, lastColRange.end))



        for ((rowIndex, value) in project.getHeaderAndValue().withIndex()) {
            val realRowIdx = startRow + rowIndex
            val row: Row = sheet.createRow(realRowIdx)

            val cellHeader: Cell = row.createCell(firstColRange.start)
            cellHeader.setCellValue(value.first)
            cellHeader.cellStyle = fontAlignCellStyle(workbook, 12)

            val cellValue: Cell = row.createCell(secondColRange.start)
            cellValue.setCellValue(value.second)


            val projectNameCell: Cell = row.createCell(lastColRange.start)
            projectNameCell.setCellValue(project.projectName)
            projectNameCell.cellStyle = fontAlignCellStyle(workbook, 25)
        }

        return cellRowRange.end + 1
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

    /**
     * TODO
     * @param scheduleExcel ScheduleExcel
     * @return ByteArray?
     */
    fun scheduleToXlsx(scheduleExcel: ScheduleExcel): ByteArray? {
        ByteArrayOutputStream().use { excelStream ->
            val headerList = Scene.displayColumns().toTypedArray()

            val workbook = SXSSFWorkbook(10000)
            workbook.isCompressTempFiles = true
            val sheet: SXSSFSheet = workbook.createSheet()


            var startRow = writeProject(workbook, sheet, scheduleExcel.project, 0, ExcelRange(0, headerList.size - 1))

            val headerStyle = headerCellStyle(workbook)
            for ((date, scenes) in scheduleExcel.sceneShootMap) {
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
