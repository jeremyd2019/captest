#!/usr/bin/env python

import sqlite3
import datetime
import errno
import os
import warnings

# data files used
# http://www2.census.gov/geo/docs/reference/state.txt
# http://www2.census.gov/geo/docs/reference/codes/files/national_county.txt
# http://www.nws.noaa.gov/geodata/catalog/wsom/data/bp10nv15.dbx
# - see http://www.nws.noaa.gov/geodata/catalog/wsom/html/cntyzone.htm

# TODO should I add marine zones?
# - see http://www.nws.noaa.gov/geodata/catalog/wsom/html/marinenwreas.htm
# TODO referential integrity issues
# - http://www.nws.noaa.gov/os/notification/scn13-75alaska_fips.htm
# - http://www.nws.noaa.gov/os/notification/scn15-44shannon_oglalacty_rem.htm

state_map = {}

try:
	os.remove("zones.sqlite")
except OSError as e:
	if (e.errno != errno.ENOENT):
		raise


with sqlite3.connect("zones.sqlite") as db:
	#db.execute("PRAGMA foreign_keys = ON")
	with open("zones.sql", "r") as f:
		db.executescript(f.read())
	db.commit()

	with open("state.txt", "r") as f:
		i = 0
		for line in f:
			i += 1
			if i == 1:
				continue
			row = line.split('|')
			state_map[row[1]] = row[0]
			try:
				db.execute("INSERT INTO state (state_id, state_abbrev, state_name) VALUES (?, ?, ?)", row[0:3])
			except Exception as e:
				print line
				raise
	db.commit()

	with open("national_county.txt", "r") as f:
		for line in f:
			row = line.split(',')
			try:
				db.execute("INSERT INTO county (state_id, county_id, county_name) VALUES (?, ?, ?)", row[1:4])
			except Exception as e:
				print line
				raise
	db.commit()

	with open("bp10nv15.dbx", "r") as f:
		for line in f:
			row = line.split('|')
			try:
				state_code = row[4][:2]
				# HACK around garbage data
				if state_code == "":
					continue
				elif state_code != "12":
					state_code = state_map[state_code]
				db.execute("INSERT OR IGNORE INTO zone (state_id, zone_id, zone_name) VALUES (?, ?, ?)", (state_code, row[1], row[3]))
				db.execute("INSERT INTO county_zone (county_state_id, county_id, zone_state_id, zone_id) SELECT :county_state_id, :county_id, :zone_state_id, :zone_id WHERE NOT EXISTS (SELECT 1 FROM county_zone WHERE county_state_id = :county_state_id AND county_id = :county_id AND zone_state_id = :zone_state_id AND zone_id = :zone_id)", {'county_state_id': row[6][:2], 'county_id': row[6][2:], 'zone_state_id': state_code, 'zone_id': row[1]})
			except Exception as e:
				print line
				raise
	db.commit()
