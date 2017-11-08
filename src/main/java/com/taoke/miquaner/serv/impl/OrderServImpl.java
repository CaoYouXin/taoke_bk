package com.taoke.miquaner.serv.impl;

import com.mysql.jdbc.StringUtils;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.ETbkOrder;
import com.taoke.miquaner.repo.TbkOrderRepo;
import com.taoke.miquaner.serv.IOrderServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.TbkOrderWrapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class OrderServImpl implements IOrderServ {

    private static final Logger logger = LogManager.getLogger(OrderServImpl.class);

    private static final String NO_TITLE_FOUND = "没找到标题";
    private static final String NO_COL_HANDLER_FOUND = "表格格式有变，请升级服务程序后再上传这批订单";

    private TbkOrderRepo tbkOrderRepo;

    @Autowired
    public OrderServImpl(TbkOrderRepo tbkOrderRepo) {
        this.tbkOrderRepo = tbkOrderRepo;
    }

    @Override
    public Object upload(String filePath) throws IOException {
        HSSFWorkbook wb = this.readFile(filePath);
        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            HSSFSheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();

            HSSFRow titleRow = sheet.getRow(0);
            if (titleRow == null) {
                return Result.fail(new ErrorR(ErrorR.NO_TITLE_FOUND, NO_TITLE_FOUND));
            }

            List<TbkOrderWrapper.TOW> towList = new ArrayList<>();
            List<Method> methodList = new ArrayList<>();
            Method[] declaredMethods = TbkOrderWrapper.class.getDeclaredMethods();
            for (int c = 0; c < titleRow.getLastCellNum(); c++) {
                HSSFCell cell = titleRow.getCell(c);
                String title = cell.getStringCellValue();
                boolean found = false;
                int count = 0;
                for (Method declaredMethod : declaredMethods) {
                    TbkOrderWrapper.TOW tow = declaredMethod.getDeclaredAnnotation(TbkOrderWrapper.TOW.class);
                    if (null == tow) {
                        logger.debug(declaredMethod.getName() + "没有TOW ANNOTATION : " + ++count);
                        continue;
                    }

                    if (!tow.value().equals(title)) {
                        continue;
                    }

                    towList.add(tow);
                    methodList.add(declaredMethod);
                    found = true;
                    break;
                }

                if (!found) {
                    logger.error(NO_COL_HANDLER_FOUND + " --> " + title);
                    return Result.fail(new ErrorR(ErrorR.NO_COL_HANDLER_FOUND, NO_COL_HANDLER_FOUND));
                }
            }

            List<ETbkOrder> toSave = new ArrayList<>();
            for (int r = 1; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                TbkOrderWrapper tbkOrderWrapper = new TbkOrderWrapper();
                HSSFCell cell = null;
                TbkOrderWrapper.TOW tow = null;
                Method method = null;
                try {
                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        cell = row.getCell(c);
                        tow = towList.get(c);
                        method = methodList.get(c);

                        processCell(tbkOrderWrapper, cell, tow, method);
                    }
                } catch (Exception e) {
                    logger.error(String.format("第%d行第%d列 cellType=%d %s TOW: %s", row.getRowNum(),
                            cell != null ? cell.getColumnIndex() : -1,
                            cell != null ? cell.getCellType() : -1,
                            method != null ? method.getName() : "unknown",
                            tow != null ? String.format("%s type=%d, sd=%b, ds=%b, dl=%b sl=%b",
                                    tow.value(), tow.type(), tow.string2date(), tow.double2string(),
                                    tow.double2long(), tow.string2long()) : "unknown"), e);
                    return Result.fail(new ErrorR(ErrorR.NO_COL_HANDLER_FOUND, NO_COL_HANDLER_FOUND));
                }

                ETbkOrder entity = tbkOrderWrapper.getEntity();
                ETbkOrder one = this.tbkOrderRepo.findByOrderIdEquals(entity.getOrderId());
                if (null == one) {
                    one = new ETbkOrder();
                }
                entity.setId(one.getId());
                BeanUtils.copyProperties(entity, one);
                toSave.add(one);
            }

            this.tbkOrderRepo.save(toSave.stream().distinct().collect(Collectors.toList()));
        }

        return Result.success(null);
    }

    private void processCell(TbkOrderWrapper tbkOrderWrapper, HSSFCell cell, TbkOrderWrapper.TOW tow, Method method) throws IllegalAccessException, InvocationTargetException, ParseException {
        switch (cell.getCellType()) {

            case 0:
                Double numericCellValue = cell.getNumericCellValue();

                if (tow.double2long()) {
                    method.invoke(tbkOrderWrapper, numericCellValue.longValue());
                    break;
                }

                if (tow.double2string()) {
                    method.invoke(tbkOrderWrapper, String.format(Locale.ENGLISH, "%.2f", numericCellValue));
                    break;
                }

                break;

            case 1:

                String stringCellValue = cell.getStringCellValue();

                if (tow.string2date()) {
                    if (!StringUtils.isNullOrEmpty(stringCellValue)) {
                        method.invoke(tbkOrderWrapper, MiquanerApplication.DEFAULT_DATE_FORMAT.parse(stringCellValue));
                    }
                    break;
                }

                if (tow.string2long()) {
                    if (!StringUtils.isNullOrEmpty(stringCellValue)) {
                        method.invoke(tbkOrderWrapper, Long.parseLong(stringCellValue));
                    }
                    break;
                }

                method.invoke(tbkOrderWrapper, stringCellValue);
                break;

            default:
//                throw new RuntimeException();
        }
    }

    private HSSFWorkbook readFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            return new HSSFWorkbook(fis);
        }
    }

}
