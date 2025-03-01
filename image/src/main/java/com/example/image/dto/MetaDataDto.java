package com.example.image.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MetaDataDto {
    private String id;
    private String filename;
    private String type;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy:MM:dd HH:mm")
    private Date uploadAt;
    private Long length;
    private Integer size;
}
