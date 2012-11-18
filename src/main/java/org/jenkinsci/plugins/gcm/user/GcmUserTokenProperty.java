package org.jenkinsci.plugins.gcm.user;

import hudson.Extension;
import hudson.Util;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;

import org.jenkinsci.plugins.gcm.im.GcmPublisher;

public class GcmUserTokenProperty extends UserProperty {

    @Extension
    public static final GcmUserPropertyDescriptor DESCRIPTOR = new GcmUserPropertyDescriptor();

    private String token;

    public GcmUserTokenProperty() {
        // Public constructor apparently needed for @Extension parsing
    }

    public GcmUserTokenProperty(String token) {
        this.token = Util.fixEmptyAndTrim(token);
    }

    public String getToken() {
        return token;
    }

    public String getGcmProjectNumber() {
        return GcmPublisher.DESCRIPTOR.getProjectNumber();
    }

    @Override
    public UserPropertyDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

}
