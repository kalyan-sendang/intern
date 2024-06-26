package com.test.exceldata.controller;

import com.test.exceldata.entity.ExcelData;
import com.test.exceldata.service.excelDataImpl.ExcelDataServiceImp;
import com.test.exceldata.service.excelDataImpl.FileUploaderServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExcelDataController {

    private final FileUploaderServiceImpl fileUploaderService;
    private final ExcelDataServiceImp excelDataService;

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpSession session) {

        Path path = fileUploaderService.uploadFile(file);
        session.setAttribute("excelFilePath", path.toString());
        return "message: "+
        "You have successfully uploaded "+ file.getOriginalFilename()+"' !";
    }

    @GetMapping("/saveData")
    public String saveExcelData(HttpSession session) {
        String filePath = (String) session.getAttribute("excelFilePath");
        if (filePath == null) {
            return "error: No file uploaded!";
        }
        List<ExcelData> excelDataAsList = excelDataService.getExcelDataAsList(filePath);
        int noOfRecords = excelDataService.saveExcelData(excelDataAsList);

        return "success: You have uploaded "+ noOfRecords +" number of records";
    }

}
