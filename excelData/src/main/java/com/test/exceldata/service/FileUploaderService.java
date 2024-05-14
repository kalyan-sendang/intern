package com.test.exceldata.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileUploaderService {
    public Path uploadFile(MultipartFile multipartFile);
}
