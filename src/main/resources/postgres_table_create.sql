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
CREATE INDEX user_profile_idx ON user_profile (user_id);
CREATE INDEX user_profile_email_idx ON user_profile (email);