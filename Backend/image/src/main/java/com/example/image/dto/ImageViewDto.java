package com.example.image.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@Builder
public class ImageViewDto {
    private Resource resource;
    private String type;
}
