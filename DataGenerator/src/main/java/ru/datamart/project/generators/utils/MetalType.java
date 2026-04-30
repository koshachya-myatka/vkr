package ru.datamart.project.generators.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MetalType {
    private static final List<String> METALS = List.of(
            "Никель (Ni)", "Медь (Cu)", "Кобальт (Co)", "Серебро (Ag)",
            "Золото (Au)", "Палладий (Pd)", "Платина (Pt)", "Родий (Rh)",
            "Иридий (Ir)", "Рутений (Ru)"
    );

    public final static List<String> METHODS = List.of(
            "Рентгенофлуоресцентный анализ", "Атомно-эмиссионная спектрометрия",
            "Испытание на растяжение", "Испытание на твёрдость",
            "Испытание на ударную вязкость", "Металлографический анализ");

    // Рентгенофлуоресцентный анализ и Атомно-эмиссионная спектрометрия (тип металла - мапа с параметрами мин-макс)
    public final static Map<String, Map<String, double[]>> XRAY_AND_ATOMIC = Map.ofEntries(
            Map.entry("Никель (Ni)", Map.ofEntries(
                    Map.entry("Никель (Ni)", new double[]{99.5, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.20}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.40}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.90}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.040}),
                    Map.entry("Сера (S)", new double[]{0, 0.040}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.30}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.06}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.10}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.005}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.001}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.001})
            )),
            Map.entry("Медь (Cu)", Map.ofEntries(
                    Map.entry("Медь (Cu)", new double[]{99.9, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.005}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.002}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.005}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.002}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.005}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.002}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.002}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.001}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.001}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.001})
            )),
            Map.entry("Кобальт (Co)", Map.ofEntries(
                    Map.entry("Кобальт (Co)", new double[]{99.35, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.005}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.05}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.10}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.005}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.20}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.40}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.10}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.70}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.001}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.001})
            )),
            Map.entry("Палладий (Pd)", Map.ofEntries(
                    Map.entry("Палладий (Pd)", new double[]{99.9, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.01}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.01}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.005}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.005}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.01}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.005}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.01}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.01}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.05})
            )),
            Map.entry("Платина (Pt)", Map.ofEntries(
                    Map.entry("Платина (Pt)", new double[]{99.95, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.01}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.01}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.005}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.005}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.01}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.005}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.01}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.01}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.05})
            )),
            Map.entry("Родий (Rh)", Map.ofEntries(
                    Map.entry("Родий (Rh)", new double[]{99.9, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.01}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.01}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.005}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.005}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.01}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.005}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.01}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.01}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.05}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.05})
            )),
            Map.entry("Иридий (Ir)", Map.ofEntries(
                    Map.entry("Иридий (Ir)", new double[]{99.8, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.01}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.01}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.005}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.005}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.01}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.005}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.01}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.01}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.05}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.05})
            )),
            Map.entry("Рутений (Ru)", Map.ofEntries(
                    Map.entry("Рутений (Ru)", new double[]{99.9, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.01}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.01}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.005}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.005}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.01}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.005}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.01}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.01}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.05}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.05})
            )),
            Map.entry("Серебро (Ag)", Map.ofEntries(
                    Map.entry("Серебро (Ag)", new double[]{99.99, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.005}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.005}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.001}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.001}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.005}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.001}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.005}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.001}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.005}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.005})
            )),
            Map.entry("Золото (Au)", Map.ofEntries(
                    Map.entry("Золото (Au)", new double[]{99.99, 100}),
                    Map.entry("Углерод (C)", new double[]{0, 0.005}),
                    Map.entry("Кремний (Si)", new double[]{0, 0.005}),
                    Map.entry("Марганец (Mn)", new double[]{0, 0.001}),
                    Map.entry("Фосфор (P)", new double[]{0, 0.001}),
                    Map.entry("Сера (S)", new double[]{0, 0.005}),
                    Map.entry("Железо (Fe)", new double[]{0, 0.005}),
                    Map.entry("Медь (Cu)", new double[]{0, 0.01}),
                    Map.entry("Хром (Cr)", new double[]{0, 0.001}),
                    Map.entry("Никель (Ni)", new double[]{0, 0.005}),
                    Map.entry("Кобальт (Co)", new double[]{0, 0.001}),
                    Map.entry("Палладий (Pd)", new double[]{0, 0.005}),
                    Map.entry("Платина (Pt)", new double[]{0, 0.005})
            ))
    );

    // Испытание на растяжение (тип металла - мапа с параметрами мин-макс)
    public final static Map<String, Map<String, double[]>> STRETCHING = Map.ofEntries(
            Map.entry("Никель (Ni)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{400, 1000}),
                    Map.entry("Предел текучести", new double[]{150, 500}),
                    Map.entry("Относительное удлинение", new double[]{30, 50})
            )),
            Map.entry("Медь (Cu)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{200, 400}),
                    Map.entry("Предел текучести", new double[]{50, 300}),
                    Map.entry("Относительное удлинение", new double[]{40, 50})
            )),
            Map.entry("Кобальт (Co)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{500, 1000}),
                    Map.entry("Предел текучести", new double[]{250, 800}),
                    Map.entry("Относительное удлинение", new double[]{15, 25})
            )),
            Map.entry("Палладий (Pd)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{200, 400}),
                    Map.entry("Предел текучести", new double[]{80, 200}),
                    Map.entry("Относительное удлинение", new double[]{35, 50})
            )),
            Map.entry("Платина (Pt)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{150, 300}),
                    Map.entry("Предел текучести", new double[]{50, 150}),
                    Map.entry("Относительное удлинение", new double[]{40, 50})
            )),
            Map.entry("Родий (Rh)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{800, 1500}),
                    Map.entry("Предел текучести", new double[]{600, 800}),
                    Map.entry("Относительное удлинение", new double[]{10, 15})
            )),
            Map.entry("Иридий (Ir)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{1000, 2000}),
                    Map.entry("Предел текучести", new double[]{800, 1000}),
                    Map.entry("Относительное удлинение", new double[]{5, 10})
            )),
            Map.entry("Рутений (Ru)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{1200, 2000}),
                    Map.entry("Предел текучести", new double[]{900, 1100}),
                    Map.entry("Относительное удлинение", new double[]{3, 5})
            )),
            Map.entry("Серебро (Ag)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{150, 250}),
                    Map.entry("Предел текучести", new double[]{30, 150}),
                    Map.entry("Относительное удлинение", new double[]{50, 70})
            )),
            Map.entry("Золото (Au)", Map.ofEntries(
                    Map.entry("Предел прочности", new double[]{120, 280}),
                    Map.entry("Предел текучести", new double[]{40, 150}),
                    Map.entry("Относительное удлинение", new double[]{40, 60})
            ))
    );

    // Испытание на твёрдость (тип металла - мапа с параметрами мин-макс)
    public final static Map<String, Map<String, double[]>> HARDNESS = Map.ofEntries(
            Map.entry("Никель (Ni)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{170, 210}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{150, 250})
            )),
            Map.entry("Медь (Cu)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{50, 150}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{50, 120})
            )),
            Map.entry("Кобальт (Co)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{200, 300}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{200, 400})
            )),
            Map.entry("Палладий (Pd)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{60, 120}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{50, 100})
            )),
            Map.entry("Платина (Pt)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{40, 100}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{40, 80})
            )),
            Map.entry("Родий (Rh)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{600, 1000}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{500, 1100})
            )),
            Map.entry("Иридий (Ir)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{600, 1200}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{500, 1300})
            )),
            Map.entry("Рутений (Ru)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{650, 1300}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{700, 1400})
            )),
            Map.entry("Серебро (Ag)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{25, 40}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{20, 35})
            )),
            Map.entry("Золото (Au)", Map.ofEntries(
                    Map.entry("Твёрдость по Бринеллю", new double[]{25, 40}),
                    Map.entry("Твёрдость по Виккерсу", new double[]{20, 35})
            ))
    );

    // Испытание на ударную вязкость (тип металла - мапа с параметрами мин-макс)
    public final static Map<String, Map<String, double[]>> IMPACT_STRENGTH = Map.ofEntries(
            Map.entry("Никель (Ni)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{120, 200})
            )),
            Map.entry("Медь (Cu)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{100, 200})
            )),
            Map.entry("Кобальт (Co)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{80, 100})
            )),
            Map.entry("Палладий (Pd)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{150, 200})
            )),
            Map.entry("Платина (Pt)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{130, 150})
            )),
            Map.entry("Родий (Rh)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{50, 80})
            )),
            Map.entry("Иридий (Ir)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{30, 50})
            )),
            Map.entry("Рутений (Ru)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{20, 40})
            )),
            Map.entry("Серебро (Ag)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{180, 200})
            )),
            Map.entry("Золото (Au)", Map.ofEntries(
                    Map.entry("Ударная вязкость по Шарпи", new double[]{160, 200})
            ))
    );

    // Металлографический анализ (тип металла - мапа с параметрами мин-макс)
    public final static Map<String, Map<String, double[]>> METALLOGRAPHIC = Map.ofEntries(
            Map.entry("Никель (Ni)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{10, 100})
            )),
            Map.entry("Медь (Cu)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{10, 150})
            )),
            Map.entry("Кобальт (Co)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{5, 50})
            )),
            Map.entry("Палладий (Pd)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{10, 80})
            )),
            Map.entry("Платина (Pt)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{10, 100})
            )),
            Map.entry("Родий (Rh)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{1, 10})
            )),
            Map.entry("Иридий (Ir)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{1, 10})
            )),
            Map.entry("Рутений (Ru)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{1, 10})
            )),
            Map.entry("Серебро (Ag)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{10, 200})
            )),
            Map.entry("Золото (Au)", Map.ofEntries(
                    Map.entry("Размер зерна", new double[]{10, 200})
            ))
    );

    public static String random() {
        return METALS.get(ThreadLocalRandom.current().nextInt(METALS.size()));
    }
}