package org.jenkinsci.plugins.gcm.im;

import hudson.plugins.im.IMException;

import java.io.PrintWriter;

/** Overrides IMException so that unrequired stack traces are not printed. */
public class GcmImException extends IMException {

    private static final long serialVersionUID = 1L;

    public GcmImException(String msg) {
        super(msg);
    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        // Print only the message, to prevent meaningless stack traces
        // from being dumped to the console for certain failures
        pw.print("     "); // Make console messages line up nicely
        pw.println(getMessage());
    }

}
