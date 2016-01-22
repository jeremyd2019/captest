-- vim: set fileencoding=utf-8 :
BEGIN;
CREATE TABLE database_version (
	database_version_id integer NOT NULL PRIMARY KEY CHECK (database_version_id = 1),
	schema_version integer not null,
	last_update_time timestamp not null
);

-- increment the schema_version when making breaking changes
INSERT INTO database_version(schema_version, last_update_time) VALUES (1, datetime('0001-01-01 00:00:00'));

PRAGMA user_version = 1;

CREATE TABLE state (
	state_id integer NOT NULL PRIMARY KEY,
	state_abbrev char(2) NOT NULL UNIQUE,
	state_name text NOT NULL
);

CREATE TABLE county (
	state_id integer NOT NULL REFERENCES state (state_id),
	county_id integer NOT NULL,
	county_name text NOT NULL,
	PRIMARY KEY (state_id, county_id)
);

CREATE TABLE zone (
	state_id integer NOT NULL REFERENCES state (state_id),
	zone_id integer NOT NULL,
	zone_name text NOT NULL,
	PRIMARY KEY (state_id, zone_id)
);

CREATE TABLE county_zone (
	county_state_id integer NOT NULL REFERENCES state (state_id),
	county_id integer NOT NULL,
	zone_state_id integer NOT NULL REFERENCES state (state_id),
	zone_id integer NOT NULL,
	FOREIGN KEY (county_state_id, county_id) REFERENCES county (state_id, county_id),
	FOREIGN KEY (zone_state_id, zone_id) REFERENCES zone (state_id, zone_id)
);
CREATE INDEX county_zone_state_id_county_id_idx ON county_zone(county_state_id, county_id);
CREATE INDEX county_zone_state_id_zone_id_idx ON county_zone(zone_state_id, zone_id);

COMMIT;
