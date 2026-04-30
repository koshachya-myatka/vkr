DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'metal_data_mart') THEN
        PERFORM dblink_exec('dbname=' || current_database(), 'CREATE DATABASE metal_data_mart');
    END IF;
END
$$;

\c metal_data_mart

CREATE TABLE IF NOT EXISTS dim_batch (
    batch_id TEXT PRIMARY KEY,
    metal_type TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    process_status INT,
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
    status INT
);
CREATE TABLE IF NOT EXISTS fact_lims (
    record_id TEXT PRIMARY KEY,
    batch_id TEXT NOT NULL REFERENCES dim_batch(batch_id),
    sample_id TEXT,
    analysis_method TEXT,
    test_date TIMESTAMP,
    status INT
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
    status INT
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

-- INSERT INTO users (name, surname) 
--     VALUES
--     ('Анна', 'Парамонова'),
--     ('Анастасия', 'Волкова'),
--     ('Вася', 'Гадов');