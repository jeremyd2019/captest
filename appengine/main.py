#!/usr/bin/env python

import webapp2
import models
from google.appengine.ext import ndb
import capparser

@ndb.tasklet
def makeAlertKeySetAndFetchAlertsAtom():
	"""
	Walk and chew gum at the same time, through cooperative multitasking.  Unfortunately results in one of those doXAndY function names.
	"""
	keyset, response = yield models.Alert.makeKeySet(), ndb.get_context().urlfetch('http://alerts.weather.gov/cap/us.php?x=0')
	raise ndb.Return((keyset, response.content))

class MainHandler(webapp2.RequestHandler):
	@ndb.toplevel
	def get(self):
		future = makeAlertKeySetAndFetchAlertsAtom()
		oldkeys, xml = future.get_result()
		parser = capparser.Parser()
		parser.alertFactory = lambda: models.Alert()
		def setAlertId(alert, id):
			alert.key = ndb.Key(models.Alert, id, parent=models.alerts_key())
		parser.setAlertId = setAlertId
		parser.geoPtFactory = models.GeoPt
		counter = [0, 0]
		def onAlertCreated(alert):
			if alert.key in oldkeys:
				oldkeys.remove(alert.key)
				counter[0] += 1
			else:
				alert.put_async()
				counter[1] += 1

		parser.onAlertCreated = onAlertCreated
		parser.parse(xml)

		ndb.delete_multi_async(oldkeys)
		self.response.write("Dupes ignored %d, put %d, deleted %d<br>\r\n" % (counter[0], counter[1], len(oldkeys)))


app = webapp2.WSGIApplication([
	('/', MainHandler)
], debug=True)
