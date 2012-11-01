package org.jenkinsci.plugins.gcm.im;

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

import org.jenkinsci.plugins.gcm.user.GcmUserTokenProperty;

public final class GcmPublisher extends IMPublisher {

    @Extension
    public static final GcmPublisherDescriptor DESCRIPTOR = new GcmPublisherDescriptor();

    GcmPublisher(List<IMMessageTarget> targets, String notificationStrategy,
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
        return GcmImConnectionProvider.getInstance().currentConnection();
    }

    @Override
    protected String getPluginName() {
        return "GCM Android notifier plugin"; // TODO where is this shown? localise
    }

    @Override
    protected String getConfiguredIMId(User user) {
        // Return the GCM token for the given Jenkins user
        GcmUserTokenProperty p = (GcmUserTokenProperty) user.getProperties().get(GcmUserTokenProperty.DESCRIPTOR);
        if (p != null) {
            return p.getToken();
        }
        return null;
    }

}
