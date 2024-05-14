package com.test.exceldata.service;


import com.test.exceldata.entity.ExcelData;

import java.util.List;

public interface ExcelDataService {
    List<ExcelData> getExcelDataAsList(String path);
    int saveExcelData(List<ExcelData>excelData);
}
