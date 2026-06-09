CREATE EXTENSION IF NOT EXISTS pg_cron;

CREATE TABLE IF NOT EXISTS fact_notifications (
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

CREATE TABLE IF NOT EXISTS dim_batch (
    batch_id TEXT PRIMARY KEY,
    metal_type TEXT NOT NULL,
    start_time TIMESTAMP,
    processing_time TIMESTAMP,
    analyses_time TIMESTAMP,
    end_time TIMESTAMP,
    process_status TEXT,
    output_yield DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS fact_mes (
    record_id TEXT PRIMARY KEY,
    order_id TEXT NOT NULL,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    equipment_id TEXT NOT NULL,
    operator_id TEXT,
    charge_mass DOUBLE PRECISION,
    output_mass DOUBLE PRECISION,
    duration_min INT
);

CREATE TABLE IF NOT EXISTS fact_lims (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    sample_id TEXT NOT NULL,
    analysis_method TEXT,
    test_date TIMESTAMP,
    status TEXT
);

CREATE TABLE IF NOT EXISTS fact_lims_results (
    id SERIAL PRIMARY KEY,
    record_id TEXT NOT NULL REFERENCES fact_lims(record_id),
    parameter_name TEXT,
    value TEXT,
    unit TEXT,
    normal BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS fact_scada (
    record_id TEXT NOT NULL,
    sensor_id TEXT NOT NULL,
    equipment_id TEXT NOT NULL,    
    time TIMESTAMP NOT NULL,
    parameter TEXT,
    value DOUBLE PRECISION,
    unit TEXT,
    status TEXT,
    PRIMARY KEY (record_id, time)
) PARTITION BY RANGE (time);

CREATE TABLE IF NOT EXISTS users (
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

CREATE INDEX IF NOT EXISTS idx_notifications_viewed
ON fact_notifications(viewed);

CREATE INDEX IF NOT EXISTS idx_notifications_created_at
ON fact_notifications(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_notifications_viewed_created
ON fact_notifications(viewed, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_batch_time_range
ON dim_batch(start_time, end_time);

CREATE INDEX IF NOT EXISTS idx_batch_status
ON dim_batch(process_status);

CREATE INDEX IF NOT EXISTS idx_mes_batch
ON fact_mes(batch_id);

CREATE INDEX IF NOT EXISTS idx_mes_equipment
ON fact_mes(equipment_id);

CREATE INDEX idx_mes_batch_equipment_covering
ON fact_mes (batch_id)
INCLUDE (equipment_id);

CREATE INDEX IF NOT EXISTS idx_mes_batch_equipment
ON fact_mes(batch_id, equipment_id);

CREATE INDEX IF NOT EXISTS idx_lims_batch
ON fact_lims(batch_id);

CREATE INDEX IF NOT EXISTS idx_lims_test_date
ON fact_lims(test_date DESC);

CREATE INDEX IF NOT EXISTS idx_lims_batch_test_date
ON fact_lims(batch_id, test_date DESC);

CREATE INDEX IF NOT EXISTS idx_lims_results_record
ON fact_lims_results(record_id);

CREATE INDEX IF NOT EXISTS idx_scada_equipment
ON fact_scada(equipment_id);

CREATE INDEX IF NOT EXISTS idx_scada_equipment_time_covering
ON fact_scada (equipment_id, time)
INCLUDE (parameter, value, unit, status);

CREATE INDEX IF NOT EXISTS idx_scada_time_brin 
ON fact_scada USING brin (time);

CREATE INDEX IF NOT EXISTS idx_scada_anomaly_status
ON fact_scada (equipment_id, time)
WHERE status IN ('WARNING', 'ALARM');

CREATE INDEX IF NOT EXISTS idx_dim_batch_dashboard_stats
ON dim_batch (process_status, metal_type);

INSERT INTO users 
    (username, password, name, surname, patronymic, email, role)
VALUES 
    ('admin', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Анна', 'Парамонова', 'Сергеевна', 'admin@gmail.com', 'ADMIN'),
    ('manager', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Никита', 'Жерновников', 'Олегович', 'manager@gmail.com', 'MANAGEMENT'),
    ('lab', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Ольга', 'Волкова', 'Станиславовна', 'lab@gmail.com', 'LABORATORY'),
    ('prod', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Сергей', 'Рыбаков', 'Александрович', 'prod@gmail.com', 'PRODUCTION');

CREATE TABLE fact_scada_default
    PARTITION OF fact_scada DEFAULT;

CREATE TABLE fact_scada_2026_05
    PARTITION OF fact_scada
    FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');

CREATE OR REPLACE FUNCTION create_next_scada_partition()
RETURNS void AS $$
DECLARE
    next_month DATE := DATE_TRUNC('month', NOW() + INTERVAL '1 month');
    partition_name TEXT;
    start_date TEXT;
    end_date TEXT;
BEGIN
    partition_name := 'fact_scada_' || TO_CHAR(next_month, 'YYYY_MM');
    start_date := TO_CHAR(next_month, 'YYYY-MM-DD');
    end_date := TO_CHAR(next_month + INTERVAL '1 month', 'YYYY-MM-DD');

    IF NOT EXISTS (
        SELECT 1 FROM pg_class WHERE relname = partition_name
    ) THEN
        EXECUTE FORMAT(
            'CREATE TABLE %I PARTITION OF fact_scada FOR VALUES FROM (%L) TO (%L)',
            partition_name, start_date, end_date
        );
    END IF;
END;
$$ LANGUAGE plpgsql;

SELECT cron.schedule('create-scada-partition', '0 9 25 * *',
    'SELECT create_next_scada_partition()');