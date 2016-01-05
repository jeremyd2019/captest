
package com.jdrake.apps.captest.xml;

import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "category")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public enum Category {

    Geo,
    Met,
    Safety,
    Security,
    Rescue,
    Fire,
    Health,
    Env,
    Transport,
    Infra,
    CBRNE,
    Other;

}
