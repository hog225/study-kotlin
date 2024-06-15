package org.yg.kotlinspring.excel.service

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.util.ObjectUtils
import org.yg.kotlinspring.excel.model.ExcelRange
import org.yg.kotlinspring.excel.model.Project
import org.yg.kotlinspring.excel.model.Scene
import org.yg.kotlinspring.excel.model.ScheduleExcel
import org.yg.kotlinspring.excel.service.ExcelStyleHelper.addBoarder
import org.yg.kotlinspring.excel.service.ExcelStyleHelper.fontAlignCellStyle
import org.yg.kotlinspring.excel.service.ExcelStyleHelper.headerCellStyle
import org.yg.kotlinspring.excel.service.ExcelStyleHelper.highlightFont
import org.yg.kotlinspring.excel.service.ExcelStyleHelper.highlightWord
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


object ScheduleExcelMaker {
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


    private fun writeProjectSector(workbook: SXSSFWorkbook, sheet: SXSSFSheet, project: Project, startRow: Int, colRange: ExcelRange): Int {
        // Col merge
        val firstColRange = ExcelRange(colRange.start, colRange.start + 1)
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, firstColRange.start, firstColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 1, startRow + 1, firstColRange.start, firstColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 2, startRow + 2, firstColRange.start, firstColRange.end))

        val secondColRange = ExcelRange(colRange.start + 2, colRange.start + 4)
        sheet.addMergedRegion(CellRangeAddress(startRow, startRow, secondColRange.start, secondColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 1, startRow + 1, secondColRange.start, secondColRange.end))
        sheet.addMergedRegion(CellRangeAddress(startRow + 2, startRow + 2, secondColRange.start, secondColRange.end))

        // Row, Col merge
        val cellRowRange = ExcelRange(startRow, startRow + 2)
        val lastColRange = ExcelRange(colRange.start + 5, colRange.end)
        sheet.addMergedRegion(CellRangeAddress(cellRowRange.start, cellRowRange.end, lastColRange.start, lastColRange.end))


        val cellHeaderStyle = fontAlignCellStyle(workbook, 12)
        val cellValueStyle = fontAlignCellStyle(workbook, 12, false)
        val projectNameStyle = fontAlignCellStyle(workbook, 25)

        for ((rowIndex, value) in project.getHeaderAndValue().withIndex()) {
            val realRowIdx = startRow + rowIndex
            val row: Row = sheet.createRow(realRowIdx)

            // Create Header
            val cellHeader: Cell = row.createCell(firstColRange.start)
            addBoarder(sheet,
                CellRangeAddress(startRow + rowIndex, startRow + rowIndex, firstColRange.start, firstColRange.end),
                cellHeaderStyle)

            cellHeader.cellStyle = cellHeaderStyle
            cellHeader.setCellValue(value.first)

            // Create Value
            val cellValue: Cell = row.createCell(secondColRange.start)
            addBoarder(sheet,
                CellRangeAddress(startRow + rowIndex, startRow + rowIndex, secondColRange.start, secondColRange.end),
                cellValueStyle)

            cellValue.cellStyle = cellValueStyle
            cellValue.setCellValue(value.second)

            // Create Title
            val projectNameCell: Cell = row.createCell(lastColRange.start)
            addBoarder(sheet,
                CellRangeAddress(cellRowRange.start, cellRowRange.end, lastColRange.start, lastColRange.end),
                projectNameStyle)

            projectNameCell.cellStyle = projectNameStyle
            projectNameCell.setCellValue(project.projectName)
        }

        return cellRowRange.end + 1
    }

    private fun writeRoundSector(workbook: SXSSFWorkbook, sheet: SXSSFSheet, date: Date, round: Int, headerStyle: CellStyle, startRow: Int, colRange: ExcelRange): Int {
        val cellRange = CellRangeAddress(startRow, startRow, colRange.start, colRange.end)
        sheet.addMergedRegion(cellRange)
        val row = sheet.createRow(startRow)
        val cell = row.createCell(colRange.start)

        headerStyle.alignment = HorizontalAlignment.LEFT
        headerStyle.verticalAlignment = VerticalAlignment.CENTER

        addBoarder(sheet, cellRange, headerStyle)
        cell.cellStyle = headerStyle

        val highlightWord = "${round}회차"
        val totalWord = getDateString(date) + " " + highlightWord
        // SXSSFWorkbook 은 RichTextString 을 지원하지 않는것 같음 그래서 하기 코드는 동작 안함
        // 다만 나중에 workbook 을 XSSFWorkbook 으로 변경하면 동작할 것으로 보임
        val cellString = highlightWord(workbook, highlightFont(workbook), totalWord, highlightWord)
        cell.setCellValue(cellString)

        return startRow + 1
    }

    private fun writeSceneSectors(sheet: SXSSFSheet, scenes: List<Scene>, headerList: List<String>, headerStyle: CellStyle, contentStyle: CellStyle, startRow: Int): Int {
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
                cell.cellStyle = contentStyle
            }
            i++
        }
        return i
    }

    /**
     * TODO
     * 표 테두리 필요
     * @param scheduleExcel ScheduleExcel
     * @return ByteArray?
     */
    fun scheduleToXlsx(scheduleExcel: ScheduleExcel): ByteArray? {
        ByteArrayOutputStream().use { excelStream ->
            val headerList = Scene.displayColumnAndSize().toTypedArray()

            val workbook = SXSSFWorkbook(10000)
            workbook.isCompressTempFiles = true
            val sheet: SXSSFSheet = workbook.createSheet()

            headerList.map { it.second }.withIndex().forEach { (index, size) ->
                sheet.setColumnWidth(index, size * 256)
            }


            var startRow = writeProjectSector(workbook, sheet, scheduleExcel.project, 0, ExcelRange(0, headerList.size - 1))

            val roundHeaderStyle = headerCellStyle(workbook)
            val sceneHeaderStyle = headerCellStyle(workbook)
            val contentStyle = ExcelStyleHelper.contentCellStyle(workbook)
            addBoarder(contentStyle)

            var round = 1
            for ((date, scenes) in scheduleExcel.sceneShootMap) {
                val roundNextRow = writeRoundSector(workbook, sheet, date, round, roundHeaderStyle, startRow, ExcelRange(0, headerList.size - 1))
                val sceneNextRow = writeSceneSectors(sheet, scenes, headerList.map { it.first }, sceneHeaderStyle, contentStyle, roundNextRow)
                startRow = sceneNextRow + 1
                round++
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
