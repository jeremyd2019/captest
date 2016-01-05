
package com.jdrake.apps.captest.xml;

import java.util.Date;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * Alert<br>
 * Generated using Android JAXB<br>
 * @link https://github.com/yeshodhan/android-jaxb
 * 
 */
@Root(name = "alert")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public class Alert {

    @Element(name = "identifier", required = true)
    private String identifier;
    @Element(name = "sender", required = true)
    private String sender;
    @Element(name = "sent", required = true)
    private Date sent;
    @Element(name = "status", required = true)
    private Status status;
    @Element(name = "msgType", required = true)
    private MsgType msgType;
    @Element(name = "source", required = false)
    private String source;
    @Element(name = "scope", required = true)
    private Scope scope;
    @Element(name = "restriction", required = false)
    private String restriction;
    @Element(name = "addresses", required = false)
    private String addresses;
    @ElementList(name = "code", entry = "code", inline = true, required = false)
    private List<String> code;
    @Element(name = "note", required = false)
    private String note;
    @Element(name = "references", required = false)
    private String references;
    @Element(name = "incidents", required = false)
    private String incidents;
    @ElementList(name = "info", entry = "info", inline = true, required = false)
    private List<Info> info;

    public Alert() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getIncidents() {
        return incidents;
    }

    public void setIncidents(String incidents) {
        this.incidents = incidents;
    }

    public List<Info> getInfo() {
        return info;
    }

    public void setInfo(List<Info> info) {
        this.info = info;
    }

}
