#!/usr/bin/env python

import webapp2
import models
import datetime
import gzip
import shutil
import os

from google.appengine.api import app_identity
from google.appengine.ext import ndb

import cloudstorage as gcs


class UploadDBUpdate(webapp2.RequestHandler):
	def post(self):
		schema_version = int(self.request.get('schema'))
		st = self.request.get('source_time')
		if st is None or st == "":
			st = '0001-01-01 00:00:00'
		st = datetime.datetime.strptime(st, "%Y-%m-%d %H:%M:%S")
		delta_file = self.request.POST["delta_file"]
		delta_file.file.seek(0, os.SEEK_END)
		delta_file_len = delta_file.file.tell()
		delta_file.file.seek(0, os.SEEK_SET)
		dbversion = models.DBUpdate.query(models.DBUpdate.schema_version == schema_version, models.DBUpdate.source_time == st).get() or models.DBUpdate()
		dbversion.schema_version = schema_version
		dbversion.source_time = st
		dbversion.delta_gs_object_name = "/%s/v%d/zones_%s.json" % (app_identity.get_default_gcs_bucket_name(), dbversion.schema_version, dbversion.source_time.isoformat().replace(':', '_'))

		# NOTE not using with intentionally, sine the docs say we only want to close() if we want the file to be committed (ie, on success only)
		options = {'content-encoding': 'gzip'} if delta_file_len > 150 else {}
		gf = gcs.open(dbversion.delta_gs_object_name, "w", content_type="application/json; charset=utf-8", options=options)
		if delta_file_len > 150:
			with gzip.GzipFile(fileobj=gf, mode="wb") as gz:
				shutil.copyfileobj(delta_file.file, gz)
		else:
			shutil.copyfileobj(delta_file.file, gf)
		gf.close()
		dbversion.put()

class UploadDBUpdateForm(webapp2.RequestHandler):
	def get(self):
		self.response.write("""<html>
<head>
<title>Upload DB Update</title>
</head>
<body>
<form action="/admin/uploadDBUpdate" method="POST" enctype="multipart/form-data">
<label for="schema">Schema Version:</label>&nbsp;<input type="text" name="schema"/><br/>
<label for="source_time">Source Timestamp:</label>&nbsp;<input type="text" name="source_time"/><br/>
<label for="delta_file">Delta File:</label><input type="file" name="delta_file"/><br/>
<input type="submit" name="submit" value="Upload"/>
</form>
</body>
</html>""")

app = webapp2.WSGIApplication([
	('/admin/uploadDBUpdate', UploadDBUpdate),
	('/admin/uploadDBUpdateForm', UploadDBUpdateForm)
], debug=True)
