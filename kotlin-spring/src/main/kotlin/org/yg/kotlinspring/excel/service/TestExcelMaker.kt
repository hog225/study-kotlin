package org.yg.kotlinspring.excel.service

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.yg.kotlinspring.excel.service.ExcellStyleHelper.fontAlignCellStyle
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import kotlin.io.path.Path


object TestExcelMaker {
    const val TMP_DIRECTORY_NAME = "tmp"

    fun createTmpDirectory() {
        if (!Files.exists(Path(TMP_DIRECTORY_NAME))) {
            Files.createDirectory(Path(TMP_DIRECTORY_NAME))
        }
    }

    fun partMergedExcelFile() {

        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet1")

        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 1))
        sheet.addMergedRegion(CellRangeAddress(1, 1, 0, 1))
        sheet.addMergedRegion(CellRangeAddress(2, 2, 0, 1))

        sheet.addMergedRegion(CellRangeAddress(0, 0, 2, 4))
        sheet.addMergedRegion(CellRangeAddress(1, 1, 2, 4))
        sheet.addMergedRegion(CellRangeAddress(2, 2, 2, 4))

        sheet.addMergedRegion(CellRangeAddress(0, 2, 5, 9))
        for (rowIndex in 0..2) {
            val row: Row = sheet.createRow(rowIndex)

            val cell: Cell = row.createCell(0)
            cell.setCellValue("Row ${rowIndex + 1}, Col 0")
            cell.cellStyle = fontAlignCellStyle(workbook, 12)

            val cell2: Cell = row.createCell(2)
            cell2.setCellValue("Row ${rowIndex + 1}, Col 1")


            val cell3: Cell = row.createCell(5)
            cell3.setCellValue("Row ${rowIndex + 1}, Col 2")
            cell3.cellStyle = fontAlignCellStyle(workbook, 25)



        }





        try {
            createTmpDirectory()
            FileOutputStream("${TMP_DIRECTORY_NAME}/merged_cells_example.xlsx").use { fileOut ->
                workbook.write(fileOut)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            workbook.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
