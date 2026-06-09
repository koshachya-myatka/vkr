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
1. Управление (все данные, админ)
2. Производство (MES + LIMS (без д.) + SCADA) [+ уведы]
3. Лаборатории (MES + LIMS, SCADA (без д.))
4. Руководство (MES + LIMS (без д.) + SCADA (без д.)) [+ уведы]

## Генератор данных
- MES - данные о состоянии партии (статус ["Поступление", "Обработка", "Лабораторный анализ", "Брак"/"Норма"], состояние при обработке)
- LIMS - данные о проведенных анализах (хим. состав, физические параметры)
- SCADA - данные о состоянии оборудования (датчики температур, давления, вибраций и т.д.)
- Связи:
    - batch_id → LIMS + MES
    - equipment_id + time → MES + SCADA

### MES | Поступают все параметры для партии в JSON | Обновление каждые 1-5 мин
Все записи - ID записи, ID заказа, ID партии, ID оборудования, ID оператора, Дата/время производства, Тип металла, Статус партии,  Масса шихты, Масса продукта, Выход годного (%)
```
{
  "record_id": "string",
  "order_id": "string",
  "batch_id": "string",
  "equipment_id": "string",
  "operator_id": "string",
  "start_time": "datetime",
  "processing_time": "datetime",
  "analyses_time": "datetime",
  "end_time": "datetime",
  "metal_type": "string",
  "process_status": "string",  
  "charge_mass": "float",
  "output_mass": "float",
  "output_yield": "float",
  "duration_min": "int"
}
```
### SCADA | Поступает по 1 параметру для оборудования в JSON | Обновление каждые 1–5 сек 
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
### Связка данных для отчета
```
{
  "batch": {
    "batch_id": "MES-2026-04-21-001",
    "metal_type": "Никель (Ni)",
    "start_time": "2026-04-21T09:00:00",
    "processing_time": "2026-04-21T09:00:00",
    "analyses_time": "2026-04-21T09:00:00",
    "end_time": "2026-04-21T11:00:00",
    "process_status": "ACCEPTED",
    "output_yield": 95.5
  },
  "mes": {   
    "order_id": "ORDER-123",
    "equipment_id": "EQ-123",
    "operator_id": "OP-114",        
    "charge_mass": "85.5",
    "output_mass": "78.2",
    "output_yield": "91",
    "duration_min": "195"
  },
  "lims": [
    {
      "sample_id": "LIM-2026-04-21-001",
      "analysis_method": "Рентгенофлуоресцентный",
      "test_date": "2026-04-21T10:30:00",
      "status": "APPROVED",
      "results": [
        {
          "parameter_name": "",
          "value": "",
          "unit: "",
          "normal": true
        },
        ...
      ]
    },
    ...
  ],  
  "scada": [
    {      
      "equipment_id": "EQ-123",            
      "parameter": "Температура",
      "avgValue" : 0.00,
      "minValue": 0.00,
      "maxValue": 0.00,
      "valuesCount": 1000
    },
    ...
  ],
  "analytics": {
    "alarm_count": 0,
    "deviation_count": 0,
    "author": "",
    "created_at": "2026-04-21T10:30:00"
  }
}
```

## Кафка
3 топика: для MES, SCADA, LIMS  

## БД
```
docker exec -it vkr-postgres-1 psql -U postgres -d metal_data_mart
```
Вьюшки для ролей: лаборатория, производство, руководство, управление (админ).  
Таблицы:
```
TABLE dim_batch (
    batch_id TEXT PRIMARY KEY,
    metal_type TEXT,
    start_time TIMESTAMP,
    processing_time TIMESTAMP,
    analyses_time TIMESTAMP,
    end_time TIMESTAMP,
    process_status TEXT,
    output_yield DOUBLE PRECISION
);
TABLE fact_mes (
    record_id TEXT PRIMARY KEY,
    order_id TEXT,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    equipment_id TEXT,
    operator_id TEXT,
    charge_mass DOUBLE PRECISION,
    output_mass DOUBLE PRECISION,
    duration_min INT
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
    record_id TEXT,
    sensor_id TEXT,
    equipment_id TEXT,    
    time TIMESTAMP,
    parameter TEXT,
    value DOUBLE PRECISION,
    unit TEXT,
    status TEXT,
    (record_id + time) PRIMARY KEY
);
TABLE fact_notifications (
    id SERIAL PRIMARY KEY,
    message TEXT,
    equipment_id TEXT,
    sensor_id TEXT,
    signal_source TEXT,   
    severity TEXT,
    status TEXT,
    comment TEXT,
    viewed BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP,
    updated_by TEXT
);
TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    name TEXT,
    surname TEXT,
    patronymic TEXT,
    email TEXT,
    role TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);
```

## Фронтенд
http://localhost:5173/login  
http://localhost:5173/register  
http://localhost:5173/profile  
http://localhost:5173/admin  
http://localhost:5173/laboratory  
http://localhost:5173/production  
http://localhost:5173/management  