#!/usr/bin/env python

from google.appengine.ext import ndb
from google.appengine.ext.ndb import GeoPt

class Alert(ndb.Model):
	#id = ndb.StringProperty(required=True)
	updated = ndb.DateTimeProperty(indexed=False)
	published = ndb.DateTimeProperty(indexed=False)
	author = ndb.StringProperty(indexed=False)
	title = ndb.StringProperty(indexed=False)
	link = ndb.StringProperty(indexed=False)
	summary = ndb.TextProperty(indexed=False)
	event = ndb.StringProperty(indexed=False)
	effective = ndb.DateTimeProperty(indexed=False)
	expires = ndb.DateTimeProperty(indexed=False)
	status = ndb.StringProperty(indexed=False, choices=("Actual", "Exercise", "System", "Test", "Draft"))
	msgType = ndb.StringProperty(indexed=False, choices=("Alert", "Update", "Cancel", "Ack", "Error"))
	category = ndb.StringProperty(indexed=False, choices=("Geo", "Met", "Safety", "Rescue", "Fire", "Health", "Env", "Transport", "Infra", "CBRNE", "Other"))
	urgency = ndb.StringProperty(indexed=False, choices=("Immediate", "Expected", "Future", "Past", "Unknown"))
	severity = ndb.StringProperty(indexed=False, choices=("Extreme", "Severe", "Moderate", "Minor", "Unknown"))
	certainty = ndb.StringProperty(indexed=False, choices=("Observed", "Likely", "Possible", "Unlikely", "Unknown"))
	areaDesc = ndb.TextProperty(indexed=False)
	polygon = ndb.GeoPtProperty(indexed=False, repeated=True)
	fipslist = ndb.StringProperty(repeated=True)
	ugclist = ndb.StringProperty(repeated=True)
	vtec = ndb.StringProperty(indexed=False)

	@classmethod
	@ndb.tasklet
	def makeKeySet(cls):
		ret = set()
		qit = cls.query().iter(keys_only=True)
		while (yield qit.has_next_async()):
			ret.add(qit.next())
		raise ndb.Return(ret)

