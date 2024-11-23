CREATE DATABASE IF NOT EXISTS health_telemetry_db;
USE health_telemetry_db;

CREATE TABLE health_metrics_by_datetime
(
user_id        String,
event_datetime DateTime64(3),
heart_rate_bpm Int16
)
ENGINE = MergeTree
PRIMARY KEY (user_id, event_datetime)
ORDER BY (user_id, event_datetime);