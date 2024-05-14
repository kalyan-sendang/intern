package com.test.exceldata.service.excelDataImpl;

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
import java.util.Objects;

@Service
public class FileUploaderServiceImpl implements FileUploaderService {
    @Value("${app.upload.dir}")
    private String uploadDir;
    @Override
    public Path uploadFile(MultipartFile file) {
        if(Boolean.TRUE.equals(isValidExcelFile(file))) {
            try {
                Path copyPathLocation = Paths.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
                System.out.println(copyPathLocation);
                Files.copy(file.getInputStream(), copyPathLocation, StandardCopyOption.REPLACE_EXISTING);
                return copyPathLocation;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not store file " + file.getOriginalFilename()
                        + ". Please try again!");
            }
        }
        return null;
    }

    public static Boolean isValidExcelFile(MultipartFile file){
        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
    }
}
