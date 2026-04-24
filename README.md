# Запуск
# Описание системы
Список металлов: никель, медь, кобальт, палладий, платина, родий, иридий, рутений, серебро, золото

Процесс по этапам:
1. Поступление партии (MES)
2. Обработка (MES, SCADA)
3. Отбор проб + Лабораторный анализ (MES, LIMS)
4. Контроль качества + Итоговая оценка (MES, LIMS, SCADA)

Определение качества:
1. Проверка соответствия ГОСТам значений из LIMS
2. Проверка соответствия пределам значений из MES
3. Проверка состояния оборудования из SCADA

Витрины по ролям:
1. Производство (MES + LIMS (без д.) + SCADA)
2. Лаборатории (MES + LIMS)
3. Руководство (MES + LIMS + SCADA)

## Генератор данных
- MES - данные о состоянии партии (статус ["Поступление", "Обработка", "Лабораторный анализ", "Брак"/"Норма"], состояние при обработке)
- LIMS - данные о проведенных анализах (хим. состав, физические параметры)
- SCADA - данные о состоянии оборудования (датчики температур, давления, вибраций и т.д.)
- Связи:
    - batch_id → LIMS + MES
    - equipment_id + time → MES + SCADA

### MES | Поступают все параметры для партии в JSON | Обновление каждые 1-5 мин
Все записи - ID записи, ID партии, ID оборудования, Дата/время производства, Тип металла, Статус партии, ID оператора, Выход годного (%)
Опционально - Температура, Давление, Время обработки, Энергопотребление, Добавки
```
{
  "record_id": "string",
  "batch_id": "string",
  "equipment_id": "string",
  "start_time": "datetime",
  "end_time": "datetime",
  "metal_type": "string",
  "process_status": "string",  
  "operator_id": "string",
  "output_yield": "float",
  "temperature": "float",
  "pressure": "float",
  "duration_sec": "int",
  "energy_consumption": "float",
  "additives": "string"
}
```
### SCADA | Поступает по 1 параметру для оборудования в JSON | Обновление каждые 1–10 сек 
Все записи - ID записи, ID датчика, ID оборудования, Время, Параметр, Значение параметра, Единица измерения значения, Статус
Параметры - Температура, Давление, Расход, Скорость, Вибрация, Электропроводность, Магнитные свойства, Газовый состав, Влажность, Напряжение/ток, Шум
```
{
  "record_id": "string",
  "sensor_id": "string",
  "equipment_id": "string",
  "time": "datetime",
  "parameter": "string",
  "value": "float",
  "unit": "string",
  "status": "normal/warning/alarm"
}
```
### LIMS | Поступает по 1 анализу для партии в JSON | Обновление 1–2 раза в час
Все записи - ID записи, ID партии, ID пробы, Дата/время анализа, Тип металла, Метод анализа, ID лаборанта, Статус пробы
```
{
  "record_id": "string",
  "batch_id": "string",
  "sample_id": "string",
  "test_date": "datetime",
  "metal_type": "string",
  "analysis_method": "string",  
  "operator_id": "string",
  "status": "string"
}
```
### Примерная полная связка данных
```
{
  "batch": {
    "batch_id": "MES-2026-04-21-001",
    "metal_type": "Никель",
    "start_time": "2026-04-21T09:00:00",
    "end_time": "2026-04-21T11:00:00",
    "process_status": "Готово",
    "output_yield": 95.5
  },
  "lims": [
    {
      "sample_id": "LIM-2026-04-21-001",
      "test_date": "2026-04-21T10:30:00",
      "analysis_method": "Рентгенофлуоресцентный",
      "status": "Одобрено",
      "chemical_composition": {
        "C": 0.05,
        "Si": 0.10,
        "Mn": 0.30,
        "P": 0.010,
        "S": 0.005,
        "Cr": 0.05,
        "Ni": 99.85
      },
      "deviations": "Нет"
    }
  ],
  "mes": {
    "equipment_id": "Печь-123",
    "operator_id": "Иванов И.И.",
    "temperature": 1450,
    "pressure": 1.2,
    "duration_sec": 7200,
    "energy_consumption": 500,
    "additives": "Алюминий (0.5%)",
    "quality_certificate": "Сертификат_MES-2026-04-21.pdf"
  },
  "scada": [
    {
      "sensor_id": "SCADA-001",
      "parameter": "Температура",
      "time": "2026-04-21T09:00:00",
      "value": 1450,
      "unit": "°C",
      "status": "normal"
    },
    {
      "sensor_id": "SCADA-002",
      "parameter": "Давление",
      "time": "2026-04-21T09:05:00",
      "value": 1.2,
      "unit": "атм",
      "status": "normal"
    }
  ],
  "analytics": {
    "compliance_status": "Соответствует",
    "trends": {
      "yield_trend": "+2%"
    }
  }
}
```

## Кафка  

## БД
? Вьюшки для ролей: лаборатория, производство, руководство.  
Таблицы:
```
TABLE dim_batch (
    batch_id TEXT PRIMARY KEY,
    metal_type TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    process_status TEXT,
    output_yield NUMERIC
);
TABLE fact_mes (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT REFERENCES dim_batch(batch_id),
    equipment_id TEXT,
    operator_id TEXT,
    temperature NUMERIC,
    pressure NUMERIC,
    duration_sec INT,
    energy_consumption NUMERIC,
    additives TEXT
);
TABLE fact_lims (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT REFERENCES dim_batch(batch_id),
    sample_id TEXT,
    analysis_method TEXT,
    test_date TIMESTAMP,
    status TEXT
);
TABLE fact_lims_results (
    id SERIAL PRIMARY KEY,
    record_id TEXT REFERENCES fact_lims(record_id),
    parameter_name TEXT,
    value NUMERIC,
    unit TEXT
);
TABLE fact_scada (
    record_id TEXT PRIMARY KEY,
    sensor_id TEXT,
    equipment_id TEXT,    
    time TIMESTAMP,
    parameter TEXT,
    value NUMERIC,
    unit TEXT,
    status TEXT
);
TABLE fact_batch_analytics (
    record_id SERIAL PRIMARY KEY,
    batch_id TEXT REFERENCES dim_batch(batch_id),
    lims_score NUMERIC,
    mes_score NUMERIC,
    scada_score NUMERIC,
    quality_score NUMERIC,
    compliance_status TEXT,
    alarm_count INT,
    deviation_count INT,
    created_at TIMESTAMP DEFAULT NOW()
);
```

## Бэкенд

## Фронтенд