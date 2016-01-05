
package com.jdrake.apps.captest.xml;

import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * Area<br>
 * Generated using Android JAXB<br>
 * @link https://github.com/yeshodhan/android-jaxb
 * 
 */
@Root(name = "area")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public class Area {

    @Element(name = "areaDesc", required = true)
    private String areaDesc;
    @ElementList(name = "polygon", entry = "polygon", inline = true, required = false)
    private List<String> polygon;
    @ElementList(name = "circle", entry = "circle", inline = true, required = false)
    private List<String> circle;
    @ElementList(name = "geocode", entry = "geocode", inline = true, required = false)
    private List<Geocode> geocode;
    @Element(name = "altitude", required = false)
    private String altitude;
    @Element(name = "ceiling", required = false)
    private String ceiling;

    public Area() {
    }

    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }

    public List<String> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<String> polygon) {
        this.polygon = polygon;
    }

    public List<String> getCircle() {
        return circle;
    }

    public void setCircle(List<String> circle) {
        this.circle = circle;
    }

    public List<Geocode> getGeocode() {
        return geocode;
    }

    public void setGeocode(List<Geocode> geocode) {
        this.geocode = geocode;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

}
