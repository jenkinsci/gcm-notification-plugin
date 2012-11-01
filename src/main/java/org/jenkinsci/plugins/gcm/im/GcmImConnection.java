package org.jenkinsci.plugins.gcm.im;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionListener;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMPresence;

import java.util.logging.Logger;

import org.jenkinsci.plugins.gcm.transport.GcmManager;

public class GcmImConnection implements IMConnection {

    private static final Logger LOGGER = Logger.getLogger(GcmImConnection.class.getName());

    @Override
    public void close() {
        // Nothing
        LOGGER.info("Connection close");
    }

    @Override
    public boolean connect() {
        LOGGER.info("Connection open, please");
        return true;
    }

    @Override
    public boolean isConnected() {
        LOGGER.info("Connection is connected?");
        return true;
    }

    @Override
    public void send(IMMessageTarget target, String text) throws IMException {
        // TODO
        String token = ((GcmMessageTarget) target).getToken();
        LOGGER.info("Send message '" + text + "' to " + target + " (token: " + token + ")");
        GcmManager.send(token, text);
    }

    @Override
    public void setPresence(IMPresence presence, String statusMessage) throws IMException {
        // TODO
        LOGGER.info("Set presence " + presence + ", with message " + statusMessage);
    }

    @Override
    public void addConnectionListener(IMConnectionListener listener) {
        // Nothing
        LOGGER.info("Add connection listener: " + listener);
    }

    @Override
    public void removeConnectionListener(IMConnectionListener listener) {
        // Nothing
        LOGGER.info("Remove connection listener: " + listener);
    }

}
