package org.jenkinsci.plugins.gcm.user;

import hudson.Extension;
import hudson.Util;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import jenkins.security.ApiTokenProperty;

import org.jenkinsci.plugins.gcm.im.GcmPublisher;

public class GcmUserTokenProperty extends UserProperty {

    @Extension
    public static final GcmUserPropertyDescriptor DESCRIPTOR = new GcmUserPropertyDescriptor();

    private String token;

    public GcmUserTokenProperty() {
        // TODO? public constructor needed for @Extension parsing
    }

    public GcmUserTokenProperty(String token) {
        token = Util.fixEmptyAndTrim(token);

        if (token != null && !isValidToken(token)) {
            throw new IllegalArgumentException("Malformed GCM token");
        }
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getGcmProjectNumber() {
        return GcmPublisher.DESCRIPTOR.getProjectNumber();
    }

    public String getUserApiToken() {
        // TODO: Security check?
        ApiTokenProperty tokenProperty = user.getProperty(ApiTokenProperty.class);
        if (tokenProperty == null) {
            return null;
        }
        return tokenProperty.getApiToken();
    }

    @Override
    public UserPropertyDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    private static final boolean isValidToken(String token) {
        return true; // TODO is there a length or character check we can do?
    }

}
