package com.jmel.inv3st0r.enums;

public enum NotificationType {
    INFO("fa-file-alt", "bg-secondary"),
    BUY("fa-dollar-sign", "bg-success"),
    SELL("fa-dollar-sign", "bg-danger"),
    FUND("fa-dollar-sign", "bg-primary");

    public final String icon;
    public final String color;

    private NotificationType(String icon, String color) {
        this.icon = icon;
        this.color = color;
    }
}
