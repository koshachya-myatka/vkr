package ru.datamart.project.models;

public enum NotificationStatusEnum {
    CREATED("СОЗДАНО"),
    IN_PROGRESS("НА ПРОВЕРКЕ"),
    FALSE_POSITIVE("ЛОЖНОЕ СРАБАТЫВАНИЕ"),
    RESOLVED("ПРИЧИНА УСТРАНЕНА");

    private final String name;

    NotificationStatusEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}