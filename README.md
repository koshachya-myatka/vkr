# Запуск
``` 
docker compose up --build -d  
docker compose down -v
docker compose start  
docker compose stop
```

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
  "status": "string" (normal/warning/alarm)
}
```
### SCADA | Поступает по 1 параметру для оборудования в JSON | Обновление каждые 1–10 сек 
Все записи - ID записи, ID датчика, ID оборудования, Время, Параметр, Значение параметра, Единица измерения значения, Статус
Параметры - Температура, Давление, Скорость, Вибрация, Влажность, Напряжение, Шум
```
{
  "record_id": "string",
  "sensor_id": "string",
  "equipment_id": "string",
  "time": "datetime",
  "parameter": "string",
  "value": "float",
  "unit": "string",
  "status": "string" (normal/warning/alarm)
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
  "status": "string" (approved/rejected),
  "results": [
    {
    "parameter_name": "string",
    "value": "string",
    "unit": "string",
    "normal": "bool"
    }
  ]
}
```
### Примерная полная связка данных
```
{
  "batch": {
    "batch_id": "MES-2026-04-21-001",
    "metal_type": "Никель (Ni)",
    "start_time": "2026-04-21T09:00:00",
    "end_time": "2026-04-21T11:00:00",
    "process_status": "ACCEPTED",
    "output_yield": 95.5
  },
  "lims": [
    {
      "sample_id": "LIM-2026-04-21-001",
      "analysis_method": "Рентгенофлуоресцентный",
      "test_date": "2026-04-21T10:30:00",
      "status": "APPROVED",
      "results": {...}
    }
  ],
  "mes": {
    "equipment_id": "Печь-123",
    "operator_id": "Иванов И.И.",
    "temperature": 1450,
    "pressure": 1.2,
    "duration_sec": 7200,
    "energy_consumption": 500,
    "status": "NORMAL"
  },
  "scada": [
    {
      "sensor_id": "SCADA-001",
      "equipment_id": "Печь-123",
      "time": "2026-04-21T09:00:00",      
      "parameter": "Температура",
      "value": 1450,
      "unit": "°C",
      "status": "NORMAL"
    },
    {
      "sensor_id": "SCADA-002",
      "equipment_id": "Печь-123",
      "time": "2026-04-21T09:05:00",
      "parameter": "Давление",
      "value": 1.2,
      "unit": "атм",
      "status": "NORMAL"
    }
  ],
  "analytics": {
    "lims_score": 100,
    "mes_score": 100,
    "scada_score": 100,
    "quality_score": 100,
    "compliance_status": 1,
    "alarm_count": 0,
    "deviation_count": 0,
    "created_at": "2026-04-21T10:30:00",
  }
}
```

## Кафка  

## БД
```
docker exec -it vkr-postgres-1 psql -U postgres -d metal_data_mart
```
Вьюшки для ролей: лаборатория, производство, руководство.  
Таблицы:
```
TABLE dim_batch (
    batch_id TEXT PRIMARY KEY,
    metal_type TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    process_status TEXT,
    output_yield DOUBLE PRECISION
);
TABLE fact_mes (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    equipment_id TEXT,
    operator_id TEXT,
    temperature DOUBLE PRECISION,
    pressure DOUBLE PRECISION,
    duration_sec INT,
    energy_consumption DOUBLE PRECISION,
    status TEXT
);
TABLE fact_lims (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    sample_id TEXT,
    analysis_method TEXT,
    test_date TIMESTAMP,
    status TEXT
);
TABLE fact_lims_results (
    id SERIAL PRIMARY KEY,
    record_id TEXT NOT NULL REFERENCES fact_lims(record_id),
    parameter_name TEXT,
    value TEXT,
    unit TEXT,
    normal BOOLEAN DEFAULT true
);
TABLE fact_scada (
    record_id TEXT PRIMARY KEY,
    sensor_id TEXT,
    equipment_id TEXT,    
    time TIMESTAMP,
    parameter TEXT,
    value DOUBLE PRECISION,
    unit TEXT,
    status TEXT
);
TABLE fact_batch_analytics (
    record_id SERIAL PRIMARY KEY,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    lims_score DOUBLE PRECISION,
    mes_score DOUBLE PRECISION,
    scada_score DOUBLE PRECISION,
    quality_score DOUBLE PRECISION,
    compliance_status TEXT,
    alarm_count INT,
    deviation_count INT,
    created_at TIMESTAMP DEFAULT NOW()
);
```

## Бэкенд

## Фронтенд
http://localhost:5173/laboratory  
http://localhost:5173/production  
http://localhost:5173/management  