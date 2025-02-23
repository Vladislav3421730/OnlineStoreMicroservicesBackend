package com.example.image.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@Builder
public class ImageDownloadDto {
    private String filename;
    private Resource resource;
    private String type;

}
