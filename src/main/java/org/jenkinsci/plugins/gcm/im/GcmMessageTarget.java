package org.jenkinsci.plugins.gcm.im;

import hudson.model.User;
import hudson.plugins.im.IMMessageTarget;

import org.jenkinsci.plugins.gcm.user.GcmUserTokenProperty;
import org.kohsuke.stapler.export.Exported;

final class GcmMessageTarget implements IMMessageTarget {

    private static final long serialVersionUID = 1L;

    private final String userId;

    /**
     * Creates a target who should receive notification messages.
     * 
     * @param userId Jenkins user ID.
     */
    GcmMessageTarget(String userId) {
        this.userId = userId;
    }

    @Exported
    public String getUserId() {
        return userId;
    }

    /** @return Returns the configured GCM token for this target, or {@code null}. */
    String getToken() {
        GcmUserTokenProperty prop = User.get(userId, false).getProperty(GcmUserTokenProperty.class);
        if (prop == null) {
            return null;
        }
        return prop.getToken();
    }

    @Override
    public String toString() {
        return getUserId();
    }

}
