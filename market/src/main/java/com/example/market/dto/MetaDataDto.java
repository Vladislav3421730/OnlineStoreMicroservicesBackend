package com.example.market.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MetaDataDto {
    private String id;
    private String filename;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy:MM:dd HH:mm")
    private Date uploadAt;
    private Long length;
    private Integer size;
}
