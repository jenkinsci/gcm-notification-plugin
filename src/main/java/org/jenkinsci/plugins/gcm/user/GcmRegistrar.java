package org.jenkinsci.plugins.gcm.user;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.model.AbstractModelObject;
import hudson.model.User;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import jenkins.model.Jenkins;

import org.acegisecurity.context.SecurityContextHolder;
import org.jenkinsci.plugins.gcm.im.GcmPublisher;
import org.json.simple.JSONObject;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebMethod;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Extension
public class GcmRegistrar extends AbstractModelObject implements UnprotectedRootAction {

    @Override
    public String getDisplayName() {
        return "GCM";
    }

    @Override
    public String getUrlName() {
        return "gcm";
    }

    // TODO: Handle errors
    public HttpResponse doRegister(@QueryParameter(required = true) final String token)
            throws ServletException, IOException {
        return new HttpResponse() {
            @Override
            public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node)
                    throws IOException, ServletException {
                rsp.setStatus(200);
                PrintWriter w = rsp.getWriter();
                User user = getCurrentUser();
                if (user != null) {
                    user.addProperty(new GcmUserTokenProperty(token));
                    user.save();
                }
                String userId = user.getId();
                w.write(String.format("OK! I got API token '%s' for user '%s'", token, userId));
            }
        };
    }

    // TODO: Proper response codes if invalid user
    @WebMethod(name = "generateQrCode.png")
    public HttpResponse doGenerateQrCode() {
        User user = getCurrentUser();
        if (user == null) {
            return null;
        }

        String userId = user.getId();
        String userApiToken = user.getProperty(GcmUserTokenProperty.class).getUserApiToken(); // TODO
        String jenkinsUrl = Jenkins.getInstance().getRootUrl();
        String googleProjectNumber = GcmPublisher.DESCRIPTOR.getProjectNumber();

        JSONObject params = new JSONObject();
        params.put("senderId", googleProjectNumber);
        params.put("url", jenkinsUrl);
        params.put("username", userId);
        params.put("token", userApiToken);

        final String content = params.toJSONString();

        return new HttpResponse() {

            @Override
            public void generateResponse(StaplerRequest req, StaplerResponse rsp, Object node)
                    throws IOException, ServletException {

                QRCodeWriter qr = new QRCodeWriter();
                BitMatrix matrix = null;
                try {
                    matrix = qr.encode(content, BarcodeFormat.QR_CODE, 400, 400);
                    rsp.setStatus(200);
                    rsp.setContentType("image/png");
                    MatrixToImageWriter.writeToStream(matrix, "png", rsp.getOutputStream());
                } catch (WriterException e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    /**
     * Determines the currently logged-in user.
     * 
     * @return The current user, or {@code null} if not logged-in.
     */
    private static User getCurrentUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return User.get(userId, false);
    }

    @Override
    public String getSearchUrl() {
        // Unused
        return null;
    }

    @Override
    public String getIconFileName() {
        // Unused
        return null;
    }

}
