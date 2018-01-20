package com.taoke.miquaner.util;

import com.taoke.miquaner.serv.impl.UserServImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportUtils {

    private static final Logger logger = LogManager.getLogger(ExportUtils.class);

    public static boolean writeFile(String filePath, List<List<String>> data, List<Integer> width) {
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet s = wb.createSheet();
            wb.setSheetName(0, "User");

            HSSFCellStyle cs = wb.createCellStyle();
            HSSFCellStyle cs2 = wb.createCellStyle();
            HSSFFont f = wb.createFont();
            HSSFFont f2 = wb.createFont();

            f.setFontHeightInPoints((short) 12);
            f.setColor(IndexedColors.BLACK.getIndex());
            f.setBold(true);
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(IndexedColors.GREY_80_PERCENT.getIndex());
            f2.setBold(false);
            cs.setFont(f);
            cs2.setFont(f2);

            int rowNum = 0, cellNum;
            HSSFRow r = s.createRow(rowNum);
            r.setHeight((short) 0x128);

            HSSFCell c = null;
            List<String> headers = data.get(0);
            for (cellNum = 0; cellNum < headers.size(); cellNum++) {
                c = r.createCell(cellNum);
                c.setCellStyle(cs);
                c.setCellValue(headers.get(cellNum));

                s.setColumnWidth(cellNum, (int) (width.get(cellNum) * 16 / 0.05));
            }

            for (rowNum = 1; rowNum < data.size(); rowNum++) {
                r = s.createRow(rowNum);
                List<String> datum = data.get(rowNum);

                for (cellNum = 0; cellNum < datum.size(); cellNum++) {
                    c = r.createCell(cellNum);
                    c.setCellStyle(cs2);
                    c.setCellValue(datum.get(cellNum));
                }
            }

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                wb.write(out);
                return true;
            }
        } catch (IOException e) {
            logger.error(e);
            return false;
        }
    }

}
