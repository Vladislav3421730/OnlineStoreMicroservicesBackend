package com.example.image.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MetaDataDto {
    private String id;
    private String filename;
    private String type;
    private Date uploadAt;
    private Long length;
    private Integer size;
}
