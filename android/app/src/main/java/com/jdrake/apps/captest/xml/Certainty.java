
package com.jdrake.apps.captest.xml;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "certainty")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public enum Certainty {

    Observed,
    Likely,
    Possible,
    Unlikely,
    Unknown;

}
