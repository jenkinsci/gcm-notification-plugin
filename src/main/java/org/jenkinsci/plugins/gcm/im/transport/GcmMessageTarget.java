package org.jenkinsci.plugins.gcm.im.transport;

import hudson.plugins.im.IMMessageTarget;


public class GcmMessageTarget implements IMMessageTarget {

    private static final long serialVersionUID = 1L;

    private final String token;

    public GcmMessageTarget(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "toString: " + token;
    }

}
