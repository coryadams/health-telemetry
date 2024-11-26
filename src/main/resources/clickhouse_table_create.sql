CREATE DATABASE IF NOT EXISTS health_telemetry_db;
USE health_telemetry_db;

CREATE TABLE health_events
(
user_id        String,
session_id     String,
event_datetime DateTime('UTC'),
heart_rate_bpm Int16
)
ENGINE = MergeTree
ORDER BY (user_id, session_id, event_datetime);

insert into health_events values ('coryadams@yahoo.com', 'sessionId_1', UTCTimestamp(), 99);
insert into health_events values ('coryadams@yahoo.com', 'sessionId_1', UTCTimestamp(), 100);
insert into health_events values ('coryadams@yahoo.com', 'sessionId_1', UTCTimestamp(), 101);