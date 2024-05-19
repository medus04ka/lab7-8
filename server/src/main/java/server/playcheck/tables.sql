BEGIN;

CREATE TYPE mood AS ENUM (
    'SADNESS', 'LONGING', 'GLOOM', 'CALM', 'RAGE'
);

CREATE TYPE weapon_type AS ENUM (
    'AXE', 'SHOTGUN', 'RIFLE', 'KNIFE', 'MACHINE_GUN'
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(52) UNIQUE NOT NULL,
    passw VARCHAR(255) NOT NULL,
    salt VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS humanbeing (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL CONSTRAINT not_empty_name CHECK(length(name) > 0),
    x INTEGER NOT NULL,
    y BIGINT NOT NULL,
    creation_date DATE DEFAULT NOW() NOT NULL,
	real_hero BOOLEAN NOT NULL,
	has_tooth_pick BOOLEAN NOT NULL,
    impact_speed BIGINT NOT NULL CONSTRAINT task_speed CHECK (impact_speed <= 659),
    weapon weapon_type,
    mood mood,
    owner_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

END;