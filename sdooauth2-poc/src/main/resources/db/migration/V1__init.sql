CREATE TABLE city (
    id   INTEGER      NOT NULL,
    name VARCHAR(128) NOT NULL,
    state VARCHAR(128) NOT NULL,
    country VARCHAR(128) NOT NULL,
    map VARCHAR(128) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE city_sequence (
  next_val bigint(20) DEFAULT NULL
);