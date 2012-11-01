package org.jenkinsci.plugins.gcm.transport;

import java.io.IOException;
import java.util.logging.Logger;

import org.jenkinsci.plugins.gcm.im.GcmPublisher;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

public class GcmManager {

    private static final Logger LOGGER = Logger.getLogger(GcmManager.class.getName());

    // TODO: This stuff should happen async, as "send" is blocking...
    public static void send(String userToken, String text) {
        // TODO: build message properly
        Message message = new Message.Builder().addData("m", text).delayWhileIdle(false).build();
        LOGGER.info("Created GCM message: " + message);

        String serverApiKey = GcmPublisher.DESCRIPTOR.getApiKey();
        LOGGER.info("Server API key: " + serverApiKey);
        Sender sender = new Sender(serverApiKey);
        try {
            LOGGER.info("Sending message...");
            sender.send(message, userToken, 1);
            LOGGER.info("Message sent!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
