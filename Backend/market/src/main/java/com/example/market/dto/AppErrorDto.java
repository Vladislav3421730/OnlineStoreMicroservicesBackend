package com.example.market.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Data
@Schema(description = "Dao for errors")
public class AppErrorDto {

    @Schema(description = "Error message describing the problem", example = "Invalid request")
    private String message;

    @Schema(description = "Timestamp of when the error occurred", example = "2025-02-07 14:30:00")
    private String timestamp;

    @Schema(description = "Error's code", examples = {"400", "404", "401", "403"})
    private int code;

    public AppErrorDto(String message, int code) {
        this.code = code;
        this.message = message;
        this.timestamp = formatTimestamp();
    }

    private String formatTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return sdf.format(new Date());
    }
}

