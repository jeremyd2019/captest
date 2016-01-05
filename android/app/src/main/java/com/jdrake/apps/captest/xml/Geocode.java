
package com.jdrake.apps.captest.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * Geocode<br>
 * Generated using Android JAXB<br>
 * @link https://github.com/yeshodhan/android-jaxb
 * 
 */
@Root(name = "geocode")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public class Geocode {

    @Element(name = "valueName", required = true)
    private String valueName;
    @Element(name = "value", required = true)
    private String value;

    public Geocode() {
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
