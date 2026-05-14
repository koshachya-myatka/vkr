DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'metal_data_mart') THEN
        PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE metal_data_mart');
    END IF;
END
$$;

\c metal_data_mart

CREATE TABLE IF NOT EXISTS fact_notifications (
    id SERIAL PRIMARY KEY,
    message TEXT,
    equipment_id TEXT, 
    signal_source TEXT,   
    severity TEXT,
    viewed BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS dim_batch (
    batch_id TEXT PRIMARY KEY,
    metal_type TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    process_status TEXT,
    output_yield DOUBLE PRECISION
);
CREATE TABLE IF NOT EXISTS fact_mes (
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
CREATE TABLE IF NOT EXISTS fact_lims (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    sample_id TEXT,
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
    record_id TEXT PRIMARY KEY,
    sensor_id TEXT,
    equipment_id TEXT,    
    time TIMESTAMP,
    parameter TEXT,
    value DOUBLE PRECISION,
    unit TEXT,
    status TEXT
);
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

CREATE INDEX IF NOT EXISTS idx_scada_equipment_time
ON fact_scada(equipment_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_scada_time_brin 
ON fact_scada USING brin (time);

CREATE INDEX IF NOT EXISTS idx_dim_batch_dashboard_stats
ON dim_batch (process_status, metal_type);

INSERT INTO users 
    (username, password, name, surname, patronymic, email, role)
VALUES 
    ('admin', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Админ', 'Админов', 'Админович', 'admin@gmail.com', 'ADMIN'),
    ('manager', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Менеджер', 'Менеджеров', 'Менеджерович', 'manager@gmail.com', 'MANAGEMENT'),
    ('lab', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Лаборант', 'Лаборантов', 'Лаборантович', 'lab@gmail.com', 'LABORATORY'),
    ('prod', '$2a$10$p3rjHEHq5vlFZ9mJhPt2mudzuqT.AaDi0nTyLITIrXmKZreBn9izq', 'Технолог', 'Технологов', 'Технологович', 'prod@gmail.com', 'PRODUCTION');