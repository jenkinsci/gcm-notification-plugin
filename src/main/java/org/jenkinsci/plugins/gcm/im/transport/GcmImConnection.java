package org.jenkinsci.plugins.gcm.im.transport;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionListener;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMPresence;

import java.util.logging.Logger;

public class GcmImConnection implements IMConnection {

    private static final Logger LOGGER = Logger.getLogger(GcmImConnection.class.getName());

    public GcmImConnection() {
        LOGGER.info("Creating GcmImConnection " + Integer.toHexString(hashCode()));
    }

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
        LOGGER.info("Send message '" + text + "' to " + target);
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
