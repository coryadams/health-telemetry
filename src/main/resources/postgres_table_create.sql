CREATE TABLE user_profile
(
    id           SERIAL PRIMARY KEY,
    user_id      varchar(125),
    password     varchar(15),
    first_name   VARCHAR(55),
    last_name    VARCHAR(55),
    email        VARCHAR(256),
    phone_number varchar(13),
    created_at   timestamp,
    updated_at   timestamp
);
CREATE UNIQUE INDEX user_profile_idx ON user_profile (user_id);
CREATE INDEX user_profile_email_idx ON user_profile (email);

CREATE TABLE activity
(
    id                  SERIAL PRIMARY KEY,
    name                varchar(125),
    description         varchar(255),
    file_name           varchar(255),
    total_time_seconds  integer,
    distance_meters      integer,
    max_speed            integer,
    calories            integer,
    avg_heart_rate_bpm  integer,
    max_heart_rate_bpm  integer,
    activity_date    timestamp,
    user_profile_id integer,
    created_at      timestamp,
    FOREIGN KEY (user_profile_id) REFERENCES user_profile(id)
);
