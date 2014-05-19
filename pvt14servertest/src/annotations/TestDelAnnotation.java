package annotations;

import com.sun.org.apache.xpath.internal.SourceTree;
import pvt14servertest.DummyLogin;
import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.DataOutputStream;
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

    private static boolean sendDeleteAnnotation() throws Exception {

        String annotation = SystemTesting.addedfields.getann();
//        String annotation = "STA-531901374344337";
        String url = SystemTesting.server+ SystemTesting.PORT
                + "/annotation/field/"+annotation;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");
//        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", dl.login().getToken());
        con.getResponseCode();

//        System.out.println("\nSending 'DELETE' request to URL : " + url);
//        System.out.println("Response Body: " + printResponse(con));

        Thread.sleep(500);
        annotations.getAnnotations();
//        System.out.println(con.getResponseCode()+"" + annotations.getResponse().contains(annotation)+annotations.getResponse());
        if(!annotations.getResponse().contains(annotation)) {
            return true;
        }
        annotations.getAnnotations();
//       System.out.println(annotations.getResponse().contains(annotation) + annotations.getResponse());
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
