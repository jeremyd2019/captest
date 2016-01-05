#!/usr/bin/env python

from xml.dom import pulldom
import dateutil.parser
from dateutil import tz

def datetime_to_utc(date):
	return date.astimezone(tz.tzutc()).replace(tzinfo=None) if date.tzinfo else date

def getText(element):
	rc = []
	for node in element.childNodes:
		if node.nodeType == node.TEXT_NODE:
			rc.append(node.data)
	return ''.join(rc)

def getFirstChildWithName(element, name):
	for node in element.childNodes:
		if node.nodeType == node.ELEMENT_NODE and node.localName == name:
			return node
	return None
def getValueForNamedValue(element, name):
	for node in element.childNodes:
		if node.nodeType == node.ELEMENT_NODE and node.localName == "valueName" and getText(node) == name:
			while node.nextSibling is not None:
				node = node.nextSibling
				if node.nodeType == node.ELEMENT_NODE and node.localName == "value":
					return getText(node)
			return None

class Parser(object):
	def __init__(self):
		self.alertFactory = lambda id: None
		self.geoPtFactory = lambda ptstr: ptstr
		self.onAlertCreated = lambda alert: None

	def parse(self, xml):
		events = pulldom.parseString(xml)
		for (event, node) in events:
			if event == pulldom.START_ELEMENT and node.tagName == "entry":
				events.expandNode(node)
				alert = self._populateAlert(node)
				self.onAlertCreated(alert)
				node.unlink()

	def _populateAlert(self, element):
		alert = self.alertFactory(getText(getFirstChildWithName(element, 'id')))
		alert.updated = datetime_to_utc(dateutil.parser.parse(getText(getFirstChildWithName(element, 'updated'))))
		alert.published = datetime_to_utc(dateutil.parser.parse(getText(getFirstChildWithName(element, 'published'))))
		alert.author = getText(getFirstChildWithName(getFirstChildWithName(element, 'author'), 'name'))
		alert.title = getText(getFirstChildWithName(element, 'title'))
		alert.link = getFirstChildWithName(element, 'link').getAttribute('href')
		alert.summary = getText(getFirstChildWithName(element, 'summary'))
		alert.event = getText(getFirstChildWithName(element, 'event'))
		alert.effective = datetime_to_utc(dateutil.parser.parse(getText(getFirstChildWithName(element, 'effective'))))
		alert.expires = datetime_to_utc(dateutil.parser.parse(getText(getFirstChildWithName(element, 'expires'))))
		alert.status = getText(getFirstChildWithName(element, 'status'))
		alert.msgType = getText(getFirstChildWithName(element, 'msgType'))
		alert.category = getText(getFirstChildWithName(element, 'category'))
		alert.severity = getText(getFirstChildWithName(element, 'severity'))
		alert.certainty = getText(getFirstChildWithName(element, 'certainty'))
		alert.areaDesc = getText(getFirstChildWithName(element, 'areaDesc'))
		alert.polygon = [self.geoPtFactory(x) for x in getText(getFirstChildWithName(element, 'polygon')).split()]
		geocode = getFirstChildWithName(element, 'geocode')
		alert.fipslist = getValueForNamedValue(geocode, 'FIPS6').split()
		alert.ugclist = getValueForNamedValue(geocode, 'UGC').split()
		# TODO could there be multiple parameter elements, each with a valueName and value element, or is it like geocode and having a list of valueName value pairs?
		alert.vtec = getValueForNamedValue(getFirstChildWithName(element, "parameter"), "VTEC")
		return alert


