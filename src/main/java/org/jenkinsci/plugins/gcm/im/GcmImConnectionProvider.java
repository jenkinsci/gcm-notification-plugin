package org.jenkinsci.plugins.gcm.im;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionProvider;
import hudson.plugins.im.IMException;

import java.util.logging.Logger;

final class GcmImConnectionProvider extends IMConnectionProvider {

    private static final Logger LOGGER = Logger.getLogger(GcmImConnectionProvider.class.getName());

    private static final IMConnectionProvider INSTANCE = new GcmImConnectionProvider();

    static final synchronized IMConnectionProvider getInstance() {
        return INSTANCE;
    }

    public GcmImConnectionProvider() {
        super();
        LOGGER.info("Creating singleton connection provider...");
        init();
    }

    @Override
    protected void init() {
        super.init();
        LOGGER.info("Initialising connection provider...");
    }

    @Override
    public synchronized IMConnection currentConnection() {
        LOGGER.info("Provider: get current connection");
        return super.currentConnection();
    }

    @Override
    public synchronized IMConnection createConnection() throws IMException {
        LOGGER.info("Provider: create new imconnection");
        return new GcmImConnection();
    }

}
