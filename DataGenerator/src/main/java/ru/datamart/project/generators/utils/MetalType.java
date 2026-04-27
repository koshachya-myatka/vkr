package ru.datamart.project.generators.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MetalType {
    private static final List<String> METALS = List.of(
            "Никель (Ni)", "Медь (Cu)", "Кобальт (Co)", "Серебро (Ag)",
            "Золото (Au)", "Палладий (Pd)", "Платина (Pt)", "Родий (Rh)",
            "Иридий (Ir)", "Рутений (Ru)"
    );

    public static String random() {
        return METALS.get(ThreadLocalRandom.current().nextInt(METALS.size()));
    }
}
