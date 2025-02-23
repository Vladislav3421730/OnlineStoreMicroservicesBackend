package com.example.market.mapper;


import com.example.market.dto.CreateImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class MultipartFileMapper {

    public static CreateImageDto map(MultipartFile file) {
        try {
            return CreateImageDto.builder()
                    .type(file.getContentType())
                    .fileName(file.getOriginalFilename())
                    .fileData(file.getBytes())
                    .filePath(UUID.randomUUID() + "_" + file.getOriginalFilename())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
