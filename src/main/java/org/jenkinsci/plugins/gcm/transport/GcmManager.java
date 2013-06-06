package org.jenkinsci.plugins.gcm.transport;

import hudson.plugins.im.IMException;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.gcm.Messages;
import org.jenkinsci.plugins.gcm.im.GcmImException;
import org.jenkinsci.plugins.gcm.im.GcmPublisher;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GcmManager {

    public static Result send(String userToken, String text) throws IMException {
        // Check whether the server token was provided
        String serverApiKey = GcmPublisher.DESCRIPTOR.getApiKey();
        if (StringUtils.isEmpty(serverApiKey)) {
            throw new GcmImException(Messages.Gcm_ServerKeyNotConfigured());
        }

        // Check whether the user has an API token
        if (StringUtils.isEmpty(userToken)) {
            throw new GcmImException(Messages.Gcm_UserTokenNotConfigured());
        }

        // For now, since we just get sent the text -- and not the Build, so we could compile some
        // more structured data for the user -- we just send the text directly to the device
        Message message = new Message.Builder().addData("m", text).delayWhileIdle(false).build();

        // Try to send message to GCM, retrying once (as sending is synchronous)
        Sender sender = new Sender(serverApiKey);
        try {
            return sender.send(message, userToken, 1);
        } catch (IOException e) {
            throw new IMException(e);
        }
    }

}
