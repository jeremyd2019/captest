#!/usr/bin/env python

import webapp2
import models
import datetime

from google.appengine.api import app_identity
from google.appengine.ext import blobstore
from google.appengine.ext import ndb
from google.appengine.ext.webapp import blobstore_handlers


class UploadDBUpdate(blobstore_handlers.BlobstoreUploadHandler):
	def post(self):
		schema_version = int(self.request.get('schema'))
		st = self.request.get('source_time')
		if st is None or st == "":
			st = '0001-01-01 00:00:00'
		st = datetime.datetime.strptime(st, "%Y-%m-%d %H:%M:%S")
		dbversion = models.DBUpdate.query(models.DBUpdate.schema_version == schema_version, models.DBUpdate.source_time == st).get() or models.DBUpdate()
		dbversion.schema_version = schema_version
		dbversion.source_time = st
		if dbversion.delta_gs_object_name is not None:
			blobstore.delete(blobstore.create_gs_key(dbversion.delta_gs_object_name))
		dbversion.delta_gs_object_name = self.get_file_infos()[0].gs_object_name
		dbversion.put()

class UploadDBUpdateForm(webapp2.RequestHandler):
	def get(self):
		self.response.write("""<html>
<head>
<title>Upload DB Update</title>
</head>
<body>
<form action="%s" method="POST" enctype="multipart/form-data">
<label for="schema">Schema Version:</label>&nbsp;<input type="text" name="schema"/><br/>
<label for="source_time">Source Timestamp:</label>&nbsp;<input type="text" name="source_time"/><br/>
<label for="delta_file">Delta File:</label><input type="file" name="delta_file"/><br/>
<input type="submit" name="submit" value="Upload"/>
</form>
</body>
</html>""" % blobstore.create_upload_url('/admin/uploadDBUpdate', gs_bucket_name=app_identity.get_default_gcs_bucket_name()))

app = webapp2.WSGIApplication([
	('/admin/uploadDBUpdate', UploadDBUpdate),
	('/admin/uploadDBUpdateForm', UploadDBUpdateForm)
], debug=True)
