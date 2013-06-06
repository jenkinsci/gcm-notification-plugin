package org.jenkinsci.plugins.gcm.im;

import hudson.model.User;
import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionListener;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMPresence;

import java.io.IOException;

import org.jenkinsci.plugins.gcm.transport.GcmManager;
import org.jenkinsci.plugins.gcm.user.GcmUserTokenProperty;

import com.google.android.gcm.server.Result;

final class GcmImConnection implements IMConnection {

    private static final GcmImConnection INSTANCE = new GcmImConnection();

    static GcmImConnection getInstance() {
        return INSTANCE;
    }

    @Override
    public void send(IMMessageTarget target, String text) throws IMException {
        GcmMessageTarget gcmTarget = (GcmMessageTarget) target;
        String gcmToken = gcmTarget.getToken();

        Result result = GcmManager.send(gcmToken, text);
        checkResult(result, gcmTarget.getUserId());
    }

    private void checkResult(Result result, String userId) throws IMException {
        if (result.getMessageId() == null
                || result.getCanonicalRegistrationId() == null)
            return;
        GcmUserTokenProperty token = new GcmUserTokenProperty(
                result.getCanonicalRegistrationId());
        User user = User.get(userId, false);
        try {
            user.addProperty(token);
            user.save();
        } catch (IOException e) {
            throw new IMException(e);
        }
    }

    @Override
    public boolean connect() {
        // Do nothing!
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void setPresence(IMPresence presence, String statusMessage)
            throws IMException {
        // Not required
    }

    @Override
    public void addConnectionListener(IMConnectionListener listener) {
        // Not required
    }

    @Override
    public void removeConnectionListener(IMConnectionListener listener) {
        // Not required
    }

    @Override
    public void close() {
        // Not required
    }

}
