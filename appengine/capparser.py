#!/usr/bin/env python

import xml.sax
from xml.sax.handler import ContentHandler
import dateutil.parser
from dateutil import tz
import cStringIO

def datetime_to_utc(date):
	return date.astimezone(tz.tzutc()).replace(tzinfo=None) if date.tzinfo else date

class Parser(ContentHandler):
	ATOM_NAMESPACE = "http://www.w3.org/2005/Atom"
	CAP_1_1_NAMESPACE = "urn:oasis:names:tc:emergency:cap:1.1"

	def __init__(self):
		self.alertFactory = lambda: None
		self.setAlertId = lambda alert, id: None
		self.geoPtFactory = lambda ptstr: ptstr
		self.onAlertCreated = lambda alert: None

		self._currentAlert = None
		self._elementStack = []
		self._curValueName = None
		self._parser = xml.sax.make_parser()
		self._parser.setContentHandler(self)
		self._parser.setFeature(xml.sax.handler.feature_namespaces, True)

	def parse(self, xml):
		self._parser.parse(cStringIO.StringIO(xml))

	def startElementNS(self, name, qname, attrs):
		self._elementStack.append([name, ""])
		if self._currentAlert is None:
			if name[0] == Parser.ATOM_NAMESPACE and name[1] == "entry":
				self._currentAlert = self.alertFactory()
		else:
			if name[0] == Parser.ATOM_NAMESPACE and name[1] == "link":
				self._currentAlert.link = attrs.getValueByQName("href")

	def endElementNS(self, name, qname):
		curElement = self._elementStack.pop()
		if self._currentAlert is not None:
			if name[0] == Parser.ATOM_NAMESPACE:
				if name[1] == "entry":
					self.onAlertCreated(self._currentAlert)
					self._currentAlert = None
				elif name[1] == "id":
					self.setAlertId(self._currentAlert, curElement[1])
				elif name[1] == "updated":
					self._currentAlert.updated = datetime_to_utc(dateutil.parser.parse(curElement[1]))
				elif name[1] == "published":
					self._currentAlert.published = datetime_to_utc(dateutil.parser.parse(curElement[1]))
				elif name[1] == "name" and self._elementStack[-1][0] == (Parser.ATOM_NAMESPACE, "author"):
					self._currentAlert.author = curElement[1]
				elif name[1] == "title":
					self._currentAlert.title = curElement[1]
				elif name[1] == "summary":
					self._currentAlert.summary = curElement[1]
					# XXX BUG in format: valueName and value are unqualified (so in the ATOM namespace) but are actually part of the CAP schema and are children of elements in the CAP namespace
				elif name[1] == "valueName":
					self._curValueName = curElement[1]
				elif name[1] == "value":
					if self._elementStack[-1][0] == (Parser.CAP_1_1_NAMESPACE, "geocode"):
						if self._curValueName == "FIPS6":
							self._currentAlert.fipslist = curElement[1].split()
						elif self._curValueName == "UGC":
							self._currentAlert.ugclist = curElement[1].split()
					elif self._elementStack[-1][0] == (Parser.CAP_1_1_NAMESPACE, "parameter"):
						if self._curValueName == "VTEC":
							self._currentAlert.vtec = curElement[1]
			elif name[0] == Parser.CAP_1_1_NAMESPACE:
				if name[1] == "event":
					self._currentAlert.event = curElement[1]
				elif name[1] == "effective":
					self._currentAlert.effective = datetime_to_utc(dateutil.parser.parse(curElement[1]))
				elif name[1] == "expires":
					self._currentAlert.expires = datetime_to_utc(dateutil.parser.parse(curElement[1]))
				elif name[1] == "status":
					self._currentAlert.status = curElement[1]
				elif name[1] == "msgType":
					self._currentAlert.msgType = curElement[1]
				elif name[1] == "category":
					self._currentAlert.category = curElement[1]
				elif name[1] == "severity":
					self._currentAlert.severity = curElement[1]
				elif name[1] == "certainty":
					self._currentAlert.certainty = curElement[1]
				elif name[1] == "areaDesc":
					self._currentAlert.areaDesc = curElement[1]
				elif name[1] == "polygon":
					self._currentAlert.polygon = map(self.geoPtFactory, curElement[1].split())


	def characters(self, content):
		self._elementStack[-1][1] += content


