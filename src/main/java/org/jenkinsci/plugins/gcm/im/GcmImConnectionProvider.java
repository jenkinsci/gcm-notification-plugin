package org.jenkinsci.plugins.gcm.im;

import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMConnectionProvider;
import hudson.plugins.im.IMException;

final class GcmImConnectionProvider extends IMConnectionProvider {

    private static final IMConnectionProvider INSTANCE = new GcmImConnectionProvider();

    static final synchronized IMConnectionProvider getInstance() {
        return INSTANCE;
    }

    public GcmImConnectionProvider() {
        super();

        // Init must be called in order to start a 'connection'
        init();
    }

    @Override
    public synchronized IMConnection createConnection() throws IMException {
        return GcmImConnection.getInstance();
    }

}
