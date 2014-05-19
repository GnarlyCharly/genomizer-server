package annotations;

import pvt14servertest.DummyLogin;
import pvt14servertest.SystemTesting;

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
public class TestDelAnnotation {
    private static DummyLogin dl= new DummyLogin();

    private static void sendDeleteAnnotation() throws Exception {

        String url = SystemTesting.server+ SystemTesting.PORT
                + "/annotation/testanno221";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", dl.login().getToken());
        con.setRequestProperty("Content-Type", "application/json");

//        System.out.println("\nSending 'DELETE' request to URL : " + url);
//        System.out.println("Response Body: " + printResponse(con));
    }
}
