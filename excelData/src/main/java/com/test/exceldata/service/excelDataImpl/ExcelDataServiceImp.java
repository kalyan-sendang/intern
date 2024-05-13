package com.test.exceldata.service.excelDataImpl;

import com.test.exceldata.entity.ExcelData;
import com.test.exceldata.repository.ExcelDataRepo;
import com.test.exceldata.service.ExcelDataService;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelDataServiceImp implements ExcelDataService {
    private final ExcelDataRepo repo;

    @Value("${app.upload.file:${user.home}}")
    private String EXCEL_FILE_PATH;
    private static final int batchSize = 1000;
    @Override
    public List<ExcelData> getExcelDataAsList() {

        List<ExcelData> excelDataList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        try(Workbook workbook = WorkbookFactory.create(new File(EXCEL_FILE_PATH))) {
            Sheet sheet = workbook.getSheetAt(0);
            int noOfColumns = sheet.getRow(0).getLastCellNum();
            List<String> list = new ArrayList<>();
            for (Row row : sheet) {
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    list.add(cellValue);
                }
            }
            excelDataList.addAll(createList(list, noOfColumns));
        }catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
        return excelDataList;
    }
    private List<ExcelData> createList(List<String> excelData, int noOfColumns){
        ArrayList<ExcelData>excelDataList = new ArrayList<>();
        int i = noOfColumns;
        do{
            ExcelData excel = new ExcelData();
            excel.setFirstName(excelData.get(i));
            excel.setLastName(excelData.get(i+1));
            excel.setGender(excelData.get(i+2));
            excel.setCountry(excelData.get(i+3));
            excel.setAge(Integer.parseInt(excelData.get(i+4)));
            excel.setDate(excelData.get(i+5));
            excel.setUserId(Long.valueOf(excelData.get(i+6)));

            excelDataList.add(excel);
            i = i + (noOfColumns);
        }while(i<excelData.size());
        return excelDataList;
    }

    @Override
    public int saveExcelData(List<ExcelData> excelData) {
        repo.saveAll(excelData);
        return excelData.size();
    }
}
