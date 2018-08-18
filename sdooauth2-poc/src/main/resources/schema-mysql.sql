CREATE TABLE IF NOT EXISTS city (
    id   INTEGER      NOT NULL,
    name VARCHAR(128) NOT NULL,
    state VARCHAR(128) NOT NULL,
    country VARCHAR(128) NOT NULL,
    map VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

create sequence IF NOT EXISTS city_sequence;