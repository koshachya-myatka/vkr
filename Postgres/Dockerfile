FROM postgres:18

RUN apt-get update && apt-get install -y \
    build-essential \
    postgresql-server-dev-18 \
    postgresql-18-cron \
    && rm -rf /var/lib/apt/lists/*

RUN echo "shared_preload_libraries = 'pg_cron'" >> /usr/share/postgresql/postgresql.conf.sample
RUN echo "cron.database_name = 'metal_data_mart'" >> /usr/share/postgresql/postgresql.conf.sample