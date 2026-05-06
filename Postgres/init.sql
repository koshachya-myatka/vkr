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
CREATE TABLE IF NOT EXISTS fact_batch_analytics (
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

CREATE INDEX IF NOT EXISTS idx_lims_results_param
ON fact_lims_results(parameter_name);

CREATE INDEX IF NOT EXISTS idx_scada_equipment_time
ON fact_scada(equipment_id, time DESC);

CREATE INDEX IF NOT EXISTS idx_scada_parameter_time
ON fact_scada(parameter, time DESC);

CREATE INDEX IF NOT EXISTS idx_scada_equipment_param_time
ON fact_scada(equipment_id, parameter, time DESC);

CREATE INDEX IF NOT EXISTS idx_scada_time
ON fact_scada(time DESC);

CREATE INDEX IF NOT EXISTS idx_batch_analytics_batch
ON fact_batch_analytics(batch_id);

CREATE INDEX IF NOT EXISTS idx_batch_analytics_created
ON fact_batch_analytics(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_batch_analytics_batch_created
ON fact_batch_analytics(batch_id, created_at DESC);



-- INSERT INTO users (name, surname) 
--     VALUES
--     ('Анна', 'Парамонова'),
--     ('Анастасия', 'Волкова'),
--     ('Вася', 'Гадов');