package ru.datamart.project.models;

import java.util.HashMap;
import java.util.Map;

public enum MetalTypeEnum {
    NI("Никель (Ni)"),
    CU("Медь (Cu)"),
    CO("Кобальт (Co)"),
    AG("Серебро (Ag)"),
    AU("Золото (Au)"),
    PD("Палладий (Pd)"),
    PT("Платина (Pt)"),
    RH("Родий (Rh)"),
    IR("Иридий (Ir)"),
    RU("Рутений (Ru)");

    private final String name;

    MetalTypeEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    private static final Map<String, MetalTypeEnum> BY_NAME = new HashMap<>();

    static {
        for (MetalTypeEnum metal : values()) {
            BY_NAME.put(metal.name, metal);
        }
    }

    public static MetalTypeEnum fromName(String name) {
        return BY_NAME.get(name);
    }
}