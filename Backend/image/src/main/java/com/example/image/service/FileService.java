package com.example.image.service;

import com.example.image.dto.ImageDownloadDto;
import com.example.image.dto.ImageViewDto;
import com.example.image.dto.MetaDataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    MetaDataDto saveFile(MultipartFile multipartFile);

    MetaDataDto saveFile(MultipartFile multipartFile, String name);

    MetaDataDto findMetaDataById(String id);

    ImageDownloadDto findFileById(String id);

    ImageViewDto findFileByName(String filename);

    Page<MetaDataDto> findAll(PageRequest pageRequest);

    void delete(String id);
}
