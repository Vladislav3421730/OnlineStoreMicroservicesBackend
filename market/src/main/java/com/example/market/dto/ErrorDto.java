package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
public abstract class ErrorDto {

    @Schema(description = "Error's code", examples = {"400", "404", "401", "403"})
    private int code;

    @Schema(description = "Timestamp of when the error occurred", example = "2025-02-07 14:30:00")
    private String timestamp;

    public ErrorDto(int code) {
        this.code = code;
        this.timestamp = formatTimestamp();
    }

    private String formatTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(new Date());
    }
}
