package org.jenkinsci.plugins.gcm.im;

import hudson.model.AutoCompletionCandidates;
import hudson.model.AbstractProject;
import hudson.model.User;
import hudson.plugins.im.IMMessageTarget;
import hudson.plugins.im.IMMessageTargetConversionException;
import hudson.plugins.im.IMMessageTargetConverter;
import hudson.plugins.im.IMPublisherDescriptor;
import hudson.plugins.im.MatrixJobMultiplier;
import hudson.plugins.im.NotificationStrategy;
import hudson.plugins.im.build_notify.BuildToChatNotifier;
import hudson.plugins.im.config.ParameterNames;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class GcmPublisherDescriptor extends BuildStepDescriptor<Publisher> implements IMPublisherDescriptor {

    private static final Logger LOGGER = Logger.getLogger(GcmPublisherDescriptor.class.getName());

    private static final String PREFIX = "gcm.";

    public static final String PARAM_PROJECT_NUMBER = PREFIX + "projectNumber";

    public static final String PARAM_API_KEY = PREFIX + "apiKey";

    public static final String PARAM_TARGETS = PREFIX + "targets";

    // TODO: Check whether these can be renamed
    public static final String[] PARAMETERVALUE_STRATEGY_VALUES = NotificationStrategy.getDisplayNames();
    public static final String PARAMETERVALUE_STRATEGY_DEFAULT = NotificationStrategy.STATECHANGE_ONLY.getDisplayName();

    private String projectNumber;

    private String apiKey;

    public GcmPublisherDescriptor() {
        super(GcmPublisher.class);
        load();
        GcmImConnectionProvider.getInstance().setDescriptor(this);
    }

    @Override
    public String getDisplayName() {
        return "Notify Android devices";
    }

    @Override
    public String getPluginDescription() {
        return "GCM plugin TODO";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getHost() {
        // Not required
        return null;
    }

    @Override
    public String getHostname() {
        // Not required
        return null;
    }

    @Override
    public int getPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getHudsonUserName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isExposePresence() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getCommandPrefix() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDefaultIdSuffix() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a new instance of {@link GcmPublisher} from a submitted form.
     */
    @Override
    public GcmPublisher newInstance(final StaplerRequest req, JSONObject formData)
            throws FormException {
        final String t = req.getParameter(PARAM_TARGETS);

        final String[] givenTargets;
        if (t == null) {
            givenTargets = new String[0];
        } else {
            givenTargets = t.split("\\s");
        }

        List<IMMessageTarget> targets = new ArrayList<IMMessageTarget>(givenTargets.length);
        for (String userId : givenTargets) {
            User user = User.get(userId, false);
            if (user != null) {
                targets.add(new GcmMessageTarget(user.getId()));
            }
        }

        String n = req.getParameter(getParamNames().getStrategy());
        if (n == null) {
            n = PARAMETERVALUE_STRATEGY_DEFAULT;
        } else {
            boolean foundStrategyValueMatch = false;
            for (final String strategyValue : PARAMETERVALUE_STRATEGY_VALUES) {
                if (strategyValue.equals(n)) {
                    foundStrategyValueMatch = true;
                    break;
                }
            }
            if (! foundStrategyValueMatch) {
                n = PARAMETERVALUE_STRATEGY_DEFAULT;
            }
        }
        boolean notifyStart = "on".equals(req.getParameter(getParamNames().getNotifyStart()));
        boolean notifySuspects = "on".equals(req.getParameter(getParamNames().getNotifySuspects()));
        boolean notifyCulprits = "on".equals(req.getParameter(getParamNames().getNotifyCulprits()));
        boolean notifyFixers = "on".equals(req.getParameter(getParamNames().getNotifyFixers()));
        boolean notifyUpstream = "on".equals(req.getParameter(getParamNames().getNotifyUpstreamCommitters()));

        MatrixJobMultiplier matrixJobMultiplier = MatrixJobMultiplier.ONLY_CONFIGURATIONS;
        if (formData.has("matrixNotifier")) {
            String o = formData.getString("matrixNotifier");
            matrixJobMultiplier = MatrixJobMultiplier.valueOf(o);
        }

        try {
            return new GcmPublisher(targets, n, notifyStart, notifySuspects, notifyCulprits,
                    notifyFixers, notifyUpstream,
                    req.bindJSON(BuildToChatNotifier.class,formData.getJSONObject("buildToChatNotifier")),
                    matrixJobMultiplier);
        } catch (final IMMessageTargetConversionException e) {
            throw new FormException(e, PARAM_TARGETS);
        }
    }

    public AutoCompletionCandidates doAutoCompleteTargets(@QueryParameter String value) {
        AutoCompletionCandidates candidates = new AutoCompletionCandidates();
        value = value.toLowerCase();
        for (User user : User.getAll()) {
            if (user == User.getUnknown()) {
                LOGGER.info("autocomplete: ignoring unknown user");
                continue;
            }

            LOGGER.info("autocomplete: checking user: " + user.getId());
            if (user.getId().toLowerCase().startsWith(value)
                    || user.getFullName().toLowerCase().startsWith(value)) {
                LOGGER.info("autocomplete: adding");
                candidates.add(user.getId());
            }
        }
        return candidates;
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        projectNumber = req.getParameter(PARAM_PROJECT_NUMBER);
        apiKey = req.getParameter(PARAM_API_KEY);
        save();
        return super.configure(req, json);
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }

    @Override
    public IMMessageTargetConverter getIMMessageTargetConverter() {
        LOGGER.info("Descriptor: get target converter");
        return new IMMessageTargetConverter() {

            @Override
            public String toString(IMMessageTarget target) {
                return target.toString();
            }

            @Override
            public IMMessageTarget fromString(String userId)
                    throws IMMessageTargetConversionException {
                return new GcmMessageTarget(userId);
            }
        };
    }

    @Override
    public List<IMMessageTarget> getDefaultTargets() {
        LOGGER.info("Descriptor: get default targets");
        return Collections.emptyList();
    }

    @Override
    public ParameterNames getParamNames() {
        return new ParameterNames() {
            @Override
            protected String getPrefix() {
                return PREFIX;
            }
        };
    }

}
