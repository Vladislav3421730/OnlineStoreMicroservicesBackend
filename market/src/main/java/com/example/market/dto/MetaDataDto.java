package com.example.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MetaDataDto {
    private String id;
    private String filename;
    private Date uploadAt;
    private Long length;
    private int size;
}
