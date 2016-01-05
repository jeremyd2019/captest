
package com.jdrake.apps.captest.xml;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "responseType")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public enum ResponseType {

    Shelter,
    Evacuate,
    Prepare,
    Execute,
    Monitor,
    Assess,
    None;

}
