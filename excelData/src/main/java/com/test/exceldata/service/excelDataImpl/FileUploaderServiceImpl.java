package com.test.exceldata.service.excelDataImpl;

import com.test.exceldata.entity.ExcelData;
import com.test.exceldata.service.FileUploaderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileUploaderServiceImpl implements FileUploaderService {
    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;
    public List<ExcelData> invoiceExcelReaderService() {
        return null;
    }
    @Override
    public void uploadFile(MultipartFile file) {
        try{
            Path copyPathLocation = Paths.get(uploadDir+ File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyPathLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }
}
