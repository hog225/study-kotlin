package org.yg.kotlinspring.excel.service

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFFont
import java.awt.Color
import java.awt.Color.*


object ExcelStyleHelper {
    fun fontAlignCellStyle(workbook: Workbook, fontSize: Short, bold: Boolean = true): CellStyle {
        val cellStyle: CellStyle = workbook.createCellStyle()
        val font: Font = workbook.createFont()
        font.fontHeightInPoints = fontSize
        font.bold = bold
        cellStyle.setFont(font)
        cellStyle.alignment = HorizontalAlignment.CENTER
        cellStyle.verticalAlignment = VerticalAlignment.CENTER
        return cellStyle
    }

    fun headerCellStyle(workbook: SXSSFWorkbook): CellStyle {
        val headerStyle = workbook.createCellStyle() as XSSFCellStyle
        val headerFont = workbook.createFont()

        headerFont.bold = true

        headerStyle.setFont(headerFont)
        val rgb = byteArrayOf(244.toByte(), 204.toByte(), 204.toByte()) // 빨강색
        headerStyle.setFillForegroundColor(XSSFColor(rgb, null))
        //headerStyle.fillForegroundColor = IndexedColors.ROSE.index

        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
        headerStyle.alignment = HorizontalAlignment.CENTER
        headerStyle.verticalAlignment = VerticalAlignment.CENTER
        addBoarder(headerStyle)

        return headerStyle
    }

    fun defaultFont(workbook: SXSSFWorkbook): Font {
        val defaultFont = workbook.createFont()
        defaultFont.color = IndexedColors.BLACK.index
        return defaultFont
    }
    fun highlightFont(workbook: SXSSFWorkbook): Font {
        val highlightFont = workbook.createFont() as XSSFFont
        highlightFont.color = Font.COLOR_RED
        return highlightFont
    }

    fun highlightWord(workbook: SXSSFWorkbook, font: Font, totalText: String, highlightText: String): RichTextString {


        val helper = workbook.creationHelper
        val richTextString = helper.createRichTextString(totalText)
        val startIndex: Int = totalText.indexOf(highlightText)

        if (startIndex >= 0) {
            val endIndex: Int = startIndex + highlightText.length
            richTextString.applyFont(startIndex, endIndex, font)

        }
        return richTextString
    }

    fun addBoarder(cellStyle: CellStyle) {
        cellStyle.borderTop = BorderStyle.THIN
        cellStyle.borderBottom = BorderStyle.THIN
        cellStyle.borderLeft = BorderStyle.THIN
        cellStyle.borderRight = BorderStyle.THIN
    }

    fun addBoarder(sheet: SXSSFSheet, cellRangeAddress: CellRangeAddress, cellStyle: CellStyle) {


            // 병합된 셀 범위의 각 셀에 테두리 스타일 적용
        for (rowNum in cellRangeAddress.firstRow..cellRangeAddress.lastRow) {
            var row: Row? = sheet.getRow(rowNum)
            if (row == null) {
                row = sheet.createRow(rowNum)
            }
            for (colNum in cellRangeAddress.firstColumn..cellRangeAddress.lastColumn) {
                var currentCell = row!!.getCell(colNum)
                if (currentCell == null) {
                    currentCell = row!!.createCell(colNum)
                }
                addBoarder(cellStyle)
                currentCell!!.cellStyle = cellStyle
            }
        }

    }

    fun contentCellStyle(workbook: SXSSFWorkbook): CellStyle {
        val contentStyle = workbook.createCellStyle() as XSSFCellStyle
        addBoarder(contentStyle)
        return contentStyle
    }
}