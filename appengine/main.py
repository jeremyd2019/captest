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
		self.response.write('Hello world!')
		future = makeAlertKeySetAndFetchAlertsAtom()
		oldkeys, xml = future.get_result()
		parser = capparser.Parser()
		parser.alertFactory = lambda id: models.Alert(parent=models.alerts_key(), id=id)
		parser.geoPtFactory = models.GeoPt
		def onAlertCreated(alert):
			if alert.key in oldkeys:
				oldkeys.remove(alert.key)
				self.response.write("Seen dupe " + repr(alert.key)+"\r\n<br/>\r\n")
			else:
				alert.put_async()
				self.response.write("Put " + repr(alert.key) + "\r\n<br/>\r\n")

		parser.onAlertCreated = onAlertCreated
		parser.parse(xml)

		ndb.delete_multi_async(oldkeys)
		self.response.write("Delete " + repr(oldkeys) + "\r\n<br/>\r\n")


app = webapp2.WSGIApplication([
	('/', MainHandler)
], debug=True)
