#!/usr/bin/env python

import endpoints
from protorpc import messages
from protorpc import message_types
from protorpc import remote

import models
from google.appengine.ext import ndb

package = 'CAP'

class GeoPtMessage(messages.Message):
	lat = messages.FloatField(1, required=True)
	lon = messages.FloatField(2, required=True)

class Alert(messages.Message):
	id = messages.StringField(1,required=True)
	updated = message_types.DateTimeField(2)
	published = message_types.DateTimeField(3)
	author = messages.StringField(4)
	title = messages.StringField(5)
	link = messages.StringField(6)
	summary = messages.StringField(7)
	event = messages.StringField(8)
	effective = message_types.DateTimeField(9)
	expires = message_types.DateTimeField(10)
	class Status(messages.Enum):
		Actual = 1
		Exercise = 2
		System = 3
		Test = 4
		Draft = 5

	status = messages.EnumField(Status, 11)
	class MsgType(messages.Enum):
		Alert = 1
		Update = 2
		Cancel = 3
		Ack = 4
		Error = 5

	msgType = messages.EnumField(MsgType, 12)
	class Category(messages.Enum):
		Geo = 1
		Met = 2
		Safety = 3
		Rescue = 4
		Fire = 5
		Health = 6
		Env = 7
		Transport = 8
		Infra = 9
		CBRNE = 10
		Other = 11

	category = messages.EnumField(Category, 13)
	class Urgency(messages.Enum):
		Immediate = 1
		Expected = 2
		Future = 3
		Past = 4
		Unknown = 5

	urgency = messages.EnumField(Urgency, 14)
	class Severity(messages.Enum):
		Extreme = 1
		Severe = 2
		Moderate = 3
		Minor = 4
		Unknown = 5

	severity = messages.EnumField(Severity, 15)
	class Certainty(messages.Enum):
		Observed = 1
		Likely = 2
		Possible = 3
		Unlikely = 4
		Unknown = 5

	certainty = messages.EnumField(Certainty, 16)
	areaDesc = messages.StringField(17)
	polygon = messages.MessageField(GeoPtMessage, 18, repeated=True)
	fipslist = messages.StringField(19, repeated=True)
	ugclist = messages.StringField(20, repeated=True)
	vtec = messages.StringField(21)


class AlertCollection(messages.Message):
	"""Collection of Alerts."""
	items = messages.MessageField(Alert, 1, repeated=True)

class AlertFilter(messages.Message):
	fips_in = messages.StringField(1, repeated=True)
	ugc_in = messages.StringField(2, repeated=True)

@endpoints.api(name='alerts', version='v1')
class AlertsApi(remote.Service):
	"""Alerts API v1."""

	@endpoints.method(AlertFilter, AlertCollection,
			path='alerts', http_method='GET',
			name='alerts.getAlerts')
	def alerts_get(self, request):
		q = models.Alert.query()
		ugc_in = None
		if request.ugc_in is not None and len(request.ugc_in) > 0:
			ugc_in = models.Alert.ugclist.IN(request.ugc_in)
		fips_in = None
		if request.fips_in is not None and len(request.fips_in) > 0:
			fips_in = models.Alert.fipslist.IN(request.fips_in)
		if ugc_in is not None and fips_in is not None:
			q = q.filter(ndb.OR(ugc_in, fips_in))
		elif ugc_in is not None:
			q = q.filter(ugc_in)
		elif fips_in is not None:
			q = q.filter(fips_in)

		ret = AlertCollection()
		for alert in q:
			a = Alert(id=alert.key.id())
			a.updated = alert.updated
			a.published = alert.published
			a.author = alert.author
			a.title = alert.title
			a.link = alert.link
			a.summary = alert.summary
			a.event = alert.event
			a.effective = alert.effective
			a.expires = alert.expires
			a.status = None if alert.status is None else Alert.Status(alert.status)
			a.msgType = None if alert.msgType is None else Alert.MsgType(alert.msgType)
			a.category = None if alert.category is None else Alert.Category(alert.category)
			a.urgency = None if alert.urgency is None else Alert.Urgency(alert.urgency)
			a.severity = None if alert.severity is None else Alert.Severity(alert.severity)
			a.certainty = None if alert.certainty is None else Alert.Certainty(alert.certainty)
			a.areaDesc = alert.areaDesc
			for pt in alert.polygon:
				a.polygon.append(GeoPtMessage(lat=pt.lat, lon=pt.lon))
			a.fipslist = alert.fipslist
			a.ugclist = alert.ugclist
			a.vtec = alert.vtec
			ret.items.append(a)
		return ret


APPLICATION = endpoints.api_server([AlertsApi])

