package com.example.market.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void sendfile(MultipartFile multipartFile,String filename);
}
