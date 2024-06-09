package org.yg.kotlinspring.excel.service

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle

object ExcellStyleHelper {
    fun fontAlignCellStyle(workbook: Workbook, fontSize: Short): CellStyle {
        val cellStyle: CellStyle = workbook.createCellStyle()
        val font: Font = workbook.createFont()
        font.fontHeightInPoints = fontSize
        font.bold = true
        cellStyle.setFont(font)
        cellStyle.alignment = HorizontalAlignment.CENTER
        cellStyle.verticalAlignment = VerticalAlignment.CENTER
        return cellStyle
    }

    fun headerCellStyle(workbook: SXSSFWorkbook): CellStyle {
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
}