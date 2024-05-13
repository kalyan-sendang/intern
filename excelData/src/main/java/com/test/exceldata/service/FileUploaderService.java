package com.test.exceldata.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploaderService {
    public void uploadFile(MultipartFile multipartFile);
}
