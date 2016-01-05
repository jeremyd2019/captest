
package com.jdrake.apps.captest.xml;

import java.util.Date;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * Info<br>
 * Generated using Android JAXB<br>
 * @link https://github.com/yeshodhan/android-jaxb
 * 
 */
@Root(name = "info")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public class Info {

    @Element(name = "language", required = false)
    private String language;
    @ElementList(name = "category", entry = "category", inline = true, required = true)
    private List<Category> category;
    @Element(name = "event", required = true)
    private String event;
    @ElementList(name = "responseType", entry = "responseType", inline = true, required = false)
    private List<ResponseType> responseType;
    @Element(name = "urgency", required = true)
    private Urgency urgency;
    @Element(name = "severity", required = true)
    private Severity severity;
    @Element(name = "certainty", required = true)
    private Certainty certainty;
    @Element(name = "audience", required = false)
    private String audience;
    @ElementList(name = "eventCode", entry = "eventCode", inline = true, required = false)
    private List<EventCode> eventCode;
    @Element(name = "effective", required = false)
    private Date effective;
    @Element(name = "onset", required = false)
    private Date onset;
    @Element(name = "expires", required = false)
    private Date expires;
    @Element(name = "senderName", required = false)
    private String senderName;
    @Element(name = "headline", required = false)
    private String headline;
    @Element(name = "description", required = false)
    private String description;
    @Element(name = "instruction", required = false)
    private String instruction;
    @Element(name = "web", required = false)
    private String web;
    @Element(name = "contact", required = false)
    private String contact;
    @ElementList(name = "parameter", entry = "parameter", inline = true, required = false)
    private List<Parameter> parameter;
    @ElementList(name = "resource", entry = "resource", inline = true, required = false)
    private List<Resource> resource;
    @ElementList(name = "area", entry = "area", inline = true, required = false)
    private List<Area> area;

    public Info() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<ResponseType> getResponseType() {
        return responseType;
    }

    public void setResponseType(List<ResponseType> responseType) {
        this.responseType = responseType;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Certainty getCertainty() {
        return certainty;
    }

    public void setCertainty(Certainty certainty) {
        this.certainty = certainty;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public List<EventCode> getEventCode() {
        return eventCode;
    }

    public void setEventCode(List<EventCode> eventCode) {
        this.eventCode = eventCode;
    }

    public Date getEffective() {
        return effective;
    }

    public void setEffective(Date effective) {
        this.effective = effective;
    }

    public Date getOnset() {
        return onset;
    }

    public void setOnset(Date onset) {
        this.onset = onset;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

    public List<Resource> getResource() {
        return resource;
    }

    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }

    public List<Area> getArea() {
        return area;
    }

    public void setArea(List<Area> area) {
        this.area = area;
    }

}
