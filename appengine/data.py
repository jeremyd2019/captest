#!/usr/bin/env python

import webapp2
from google.appengine.api import app_identity
import re, time, urllib, base64

class DataHandler(webapp2.RequestHandler):
	since_regex = re.compile(r'(\d{4}-\d{2}-\d{2}) (\d{2}):(\d{2}):(\d{2})')

	def get(self):
		schema_ver = self.request.get('schema')
		since = self.request.get('since')
		since = "0000-00-00 00:00:00" if since is None or since == "" else since
		match = type(self).since_regex.match(since)
		if match is None:
			since = "0000-00-00T00_00_00"
		else:
			since = "%sT%s_%s_%s" % match.group(1,2,3,4)

		bucket = app_identity.get_default_gcs_bucket_name()
		relurl = "/%s/v%d/zones_%s.json" % (bucket, int(schema_ver), since)
		expiry = int(time.time()) + 300
		string_to_sign = "GET\n\n\n%d\n%s" % (expiry, relurl)
		signature = app_identity.sign_blob(str(string_to_sign))[1]
		query_params = {'GoogleAccessId': app_identity.get_service_account_name(), 'Expires': str(expiry), 'Signature': base64.b64encode(signature)}
		url = 'https://storage.googleapis.com%s?%s' % (relurl, urllib.urlencode(query_params))
		return self.redirect(str(url))


app = webapp2.WSGIApplication([
	('/data.cgi', DataHandler),
], debug=True)
