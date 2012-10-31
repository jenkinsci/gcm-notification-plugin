package org.jenkinsci.plugins.gcm.im.transport;

import hudson.Extension;
import hudson.model.User;
import hudson.plugins.im.IMConnection;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMMessageTargetConversionException;
import hudson.plugins.im.MatrixJobMultiplier;
import hudson.plugins.im.IMPublisher;
import hudson.plugins.im.build_notify.BuildToChatNotifier;

import java.util.List;
import java.util.logging.Logger;

import org.jenkinsci.plugins.gcm.user.GcmUserProperty;

public class GcmPublisher extends IMPublisher {

    private static final Logger LOGGER = Logger.getLogger(GcmPublisher.class.getName());

    @Extension
    public static final GcmPublisherDescriptor DESCRIPTOR = new GcmPublisherDescriptor();

    public GcmPublisher(List<IMMessageTarget> targets, String notificationStrategy,
            boolean notifyGroupChatsOnBuildStart, boolean notifySuspects, boolean notifyCulprits,
            boolean notifyFixers, boolean notifyUpstreamCommitters,
            BuildToChatNotifier buildToChatNotifier, MatrixJobMultiplier matrixJobMultiplier)
            throws IMMessageTargetConversionException {
        super(targets, notificationStrategy, notifyGroupChatsOnBuildStart, notifySuspects,
                notifyCulprits, notifyFixers, notifyUpstreamCommitters, buildToChatNotifier,
                matrixJobMultiplier);
    }

    @Override
    public GcmPublisherDescriptor getDescriptor() {
        return GcmPublisher.DESCRIPTOR;
    }

    @Override
    protected IMConnection getIMConnection() throws IMException {
        LOGGER.info("Publisher: get connection");
        return GcmImConnectionProvider.getInstance().currentConnection();
    }

    @Override
    protected String getPluginName() {
        return "GCM Android notifier plugin"; // TODO localise
    }

    @Override
    public List<IMMessageTarget> getNotificationTargets() {
        LOGGER.info("Publisher: get notification targets");
        return super.getNotificationTargets();
    }

    @Override
    protected String getConfiguredIMId(User user) {
        LOGGER.info("Publisher: get IM info for user: " + user.toString());

        // Return the GCM token for the given Jenkins user
        GcmUserProperty p = (GcmUserProperty) user.getProperties().get(GcmUserProperty.DESCRIPTOR);
        if (p != null) {
            return p.getToken();
        }
        return null;
    }

}
