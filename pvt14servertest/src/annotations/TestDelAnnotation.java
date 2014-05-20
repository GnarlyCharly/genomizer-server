package annotations;

import pvt14servertest.DummyLogin;
import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project: genomizer-Server
 * Package: annotations
 * User: c08esn
 * Date: 5/19/14
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestDelAnnotation extends Thread {
    private static DummyLogin dl= new DummyLogin();
    private static TestAnnotations annotations = new TestAnnotations();

    @SuppressWarnings("AccessStaticViaInstance")
    private static boolean sendDeleteAnnotation() throws Exception {

        String annotation = SystemTesting.addedfields.getann();
        String url = SystemTesting.server+ SystemTesting.PORT
                + "/annotation/field/"+annotation;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");
        con.setRequestProperty("Authorization", dl.login().getToken());
        con.getResponseCode();

        annotations.getAnnotations();
        if(!annotations.getResponse().contains(annotation)) {
            return true;
        }
        annotations.getAnnotations();
        resultClass.getInstance().addError("Ánnotation still exists: "+annotation);
        return false;


    }


    @Override
    public void run() {
        for (int i = 0; i < Values.NLOOPS; i++) {
            try {
                Values.incdelannotot();
                if (sendDeleteAnnotation()) {
                    Values.incdelannoacc();
                }

            } catch (Exception e) {

                resultClass.getInstance().addError(e.getMessage());
                // System.err.println("ERROR, received: " + response);
                // e.printStackTrace();
            }
        }
    }
}
