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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelDataServiceImp implements ExcelDataService {
    private final ExcelDataRepo repo;

//    @Value("${app.upload.file:${user.home}}")
//    private String EXCEL_FILE_PATH;
    @Override
    public List<ExcelData> getExcelDataAsList(String path) {

        List<ExcelData> excelDataList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        try(Workbook workbook = WorkbookFactory.create(new File(path))) {
            Sheet sheet = workbook.getSheetAt(0);
            int noOfColumns = sheet.getRow(0).getLastCellNum();
            List<String> list = new ArrayList<>();
            int rowIndex =0;
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
                excel.setFirstName(getValueOrNull(excelData, i));
                excel.setLastName(getValueOrNull(excelData, i + 1));
                excel.setGender(getValueOrNull(excelData, i + 2));
                excel.setCountry(getValueOrNull(excelData, i + 3));
                excel.setAge(getIntegerOrNull(excelData, i + 4));
                excel.setDate(getValueOrNull(excelData, i + 5));
                excel.setUserId(getLongOrNull(excelData, i + 6));

            excelDataList.add(excel);
            i = i + (noOfColumns);
        }while(i<excelData.size());
        return excelDataList;
    }
    private String getValueOrNull(List<String> excelData, int index) {
        if (index >= excelData.size()) {
            return null;
        }
        String value = excelData.get(index);
        return value != null && !value.isEmpty() ? value : null;
    }

    private Integer getIntegerOrNull(List<String> excelData, int index) {
        String valueOrNull = getValueOrNull(excelData, index);
        return valueOrNull != null ? Integer.parseInt(valueOrNull) : null;
    }

    private Long getLongOrNull(List<String> excelData, int index) {
        String valueOrNull = getValueOrNull(excelData, index);
        return valueOrNull != null ? Long.parseLong(valueOrNull) : null;
    }

    @Override
    public int saveExcelData(List<ExcelData> excelData) {

        List<ExcelData> validData = excelData.stream()
                .filter(this::isValidExcelData)
                .toList();

        if (validData.size() < excelData.size()) {
            // Handle validation errors
            throw new RuntimeException("Some Excel data is invalid. Please check and try again.");
        }
        repo.saveAll(excelData);
        return excelData.size();
    }

    private boolean isValidExcelData(ExcelData data) {
        // Implement validation logic here
        return isValidString(data.getFirstName()) &&
                isValidString(data.getLastName()) &&
                isValidString(data.getGender()) &&
                isValidString(data.getCountry()) &&
                data.getAge() >= 0 &&
                isValidString(data.getDate()) &&
                data.getUserId() != null;
    }

    private boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
