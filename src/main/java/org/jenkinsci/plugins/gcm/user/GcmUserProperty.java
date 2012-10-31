package org.jenkinsci.plugins.gcm.user;

import hudson.Extension;
import hudson.Util;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

@ExportedBean(defaultVisibility = 999)
public class GcmUserProperty extends UserProperty {

    @Extension
    public static final GcmUserPropertyDescriptor DESCRIPTOR = new GcmUserPropertyDescriptor();

    private String token;

    public GcmUserProperty() {
        // TODO? public constructor needed for @Extension parsing
    }

    public GcmUserProperty(String token) {
        token = Util.fixEmptyAndTrim(token);

        if (token != null && !isValidToken(token)) {
            throw new IllegalArgumentException("Malformed GCM token");
        }
        this.token = token;
    }

    @Exported
    public String getToken() {
        return token;
    }

    @Override
    public UserPropertyDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    private static final boolean isValidToken(String token) {
        return true; // TODO is there a length or character check we can do?
    }

}
