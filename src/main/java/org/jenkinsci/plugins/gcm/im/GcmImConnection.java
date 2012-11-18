package org.jenkinsci.plugins.gcm.im;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionListener;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMPresence;

import org.jenkinsci.plugins.gcm.transport.GcmManager;

final class GcmImConnection implements IMConnection {

    private static final GcmImConnection INSTANCE = new GcmImConnection();

    static GcmImConnection getInstance() {
        return INSTANCE;
    }

    @Override
    public void send(IMMessageTarget target, String text) throws IMException {
        String gcmToken = ((GcmMessageTarget) target).getToken();
        GcmManager.send(gcmToken, text);
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
    public void setPresence(IMPresence presence, String statusMessage) throws IMException {
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
