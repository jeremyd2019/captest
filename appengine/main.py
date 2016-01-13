#!/usr/bin/env python

import webapp2
import models
from google.appengine.api import memcache
from google.appengine.ext import ndb
import capparser

@ndb.tasklet
def fetchAlertsAtom():
	response = yield ndb.get_context().urlfetch('http://alerts.weather.gov/cap/us.php?x=0')
	raise ndb.Return(response.content)

class MainHandler(webapp2.RequestHandler):
	@ndb.toplevel
	def get(self):
		xmlfuture = fetchAlertsAtom()
		oldkeys = memcache.get('keys')
		if oldkeys is None:
			oldkeys = models.Alert.makeKeySet().get_result()
		parser = capparser.Parser()
		parser.alertFactory = lambda: models.Alert()
		def setAlertId(alert, id):
			alert.key = ndb.Key(models.Alert, id)
		parser.setAlertId = setAlertId
		parser.geoPtFactory = models.GeoPt
		counter = [0, 0]
		curkeys = set()
		def onAlertCreated(alert):
			curkeys.add(alert.key)
			if alert.key in oldkeys:
				oldkeys.remove(alert.key)
				counter[0] += 1
			else:
				alert.put_async()
				counter[1] += 1

		parser.onAlertCreated = onAlertCreated
		parser.parse(xmlfuture.get_result())

		ndb.delete_multi_async(oldkeys)
		self.response.write("Dupes ignored %d, put %d, deleted %d<br>\r\n" % (counter[0], counter[1], len(oldkeys)))
		memcache.set('keys', curkeys)


app = webapp2.WSGIApplication([
	('/', MainHandler)
], debug=True)
