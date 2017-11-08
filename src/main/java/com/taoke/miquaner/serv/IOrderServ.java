package com.taoke.miquaner.serv;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;

public interface IOrderServ {

    Object upload(String filePath) throws IOException;

}
