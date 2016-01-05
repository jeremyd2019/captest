
package com.jdrake.apps.captest.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


/**
 * Resource<br>
 * Generated using Android JAXB<br>
 * @link https://github.com/yeshodhan/android-jaxb
 * 
 */
@Root(name = "resource")
@Namespace(reference = "urn:oasis:names:tc:emergency:cap:1.1")
public class Resource {

    @Element(name = "resourceDesc", required = true)
    private String resourceDesc;
    @Element(name = "mimeType", required = false)
    private String mimeType;
    @Element(name = "size", required = false)
    private Integer size;
    @Element(name = "uri", required = false)
    private String uri;
    @Element(name = "derefUri", required = false)
    private String derefUri;
    @Element(name = "digest", required = false)
    private String digest;

    public Resource() {
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDerefUri() {
        return derefUri;
    }

    public void setDerefUri(String derefUri) {
        this.derefUri = derefUri;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

}
