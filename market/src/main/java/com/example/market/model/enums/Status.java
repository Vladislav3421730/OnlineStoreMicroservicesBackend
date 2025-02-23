package com.example.market.model.enums;

public enum Status {

    ACCEPTED("принят"),
    IN_PROCESSING("в обработке"),
    IN_TRANSIT("в пути"),
    DELIVERED("доставлен");

    private final String rusStatus;

    Status(String status) {
        this.rusStatus = status;
    }

    public String getDisplayName() {
        return rusStatus;
    }
}
