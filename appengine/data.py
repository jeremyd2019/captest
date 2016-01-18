#!/usr/bin/env python

import webapp2
import models
import datetime

from google.appengine.api import app_identity
from google.appengine.ext import ndb
import time, urllib, base64

import cloudstorage.common

api_url = cloudstorage.common.local_api_url() if cloudstorage.common.local_run() else "https://storage.googleapis.com"

class DataHandler(webapp2.RequestHandler):
	def get(self):
		schema_ver = int(self.request.get('schema'))
		since = self.request.get('since')
		since = "0001-01-01 00:00:00" if since is None or since == "" else since
		since = datetime.datetime.strptime(since, "%Y-%m-%d %H:%M:%S")
		dbversion = models.DBUpdate.query(models.DBUpdate.schema_version == schema_ver, models.DBUpdate.source_time <= since).order(-models.DBUpdate.source_time).get()

		expiry = int(time.time()) + 30
		obj = dbversion.delta_gs_object_name
		obj = obj[3:] if obj[:4] == "/gs/" else obj
		string_to_sign = "GET\n\n\n%d\n%s" % (expiry, obj)
		signature = app_identity.sign_blob(str(string_to_sign))[1]
		query_params = {'GoogleAccessId': app_identity.get_service_account_name(), 'Expires': str(expiry), 'Signature': base64.b64encode(signature)}
		url = '%s%s?%s' % (api_url, obj, urllib.urlencode(query_params))
		return self.redirect(str(url))


app = webapp2.WSGIApplication([
	('/data.cgi', DataHandler),
], debug=True)
