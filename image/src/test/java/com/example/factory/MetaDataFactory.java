package com.example.factory;

import com.example.image.dto.ImageDownloadDto;
import com.example.image.dto.ImageViewDto;
import com.example.image.dto.MetaDataDto;

public class MetaDataFactory {

    public static MetaDataDto getSavingMetaDataDto() {
        return MetaDataDto.builder()
                .type("image/jpeg")
                .filename("saved_file")
                .id("saving_id")
                .build();
    }

    public static MetaDataDto getMetaDataDto() {
        return MetaDataDto.builder()
                .type("image/jpeg")
                .filename("file")
                .id("id")
                .build();
    }

    public static ImageDownloadDto getImageDownloadDto() {
        return ImageDownloadDto.builder()
                .type("image/jpeg")
                .filename("filename")
                .build();
    }

    public static ImageViewDto getImageViewDto() {
        return ImageViewDto.builder()
                .type("image/jpeg")
                .build();
    }
}
