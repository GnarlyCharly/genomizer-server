package annotations;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import pvt14servertest.*;

import java.io.DataOutputStream;
import java.io.IOException;
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
public class TestAddAnnotation extends Thread {

    private static DummyLogin dl = new DummyLogin();


    @SuppressWarnings("AccessStaticViaInstance")
    private static boolean sendAddAnnotation() throws Exception {
        TestAnnotations testann = new TestAnnotations();
        JsonArray ja = new JsonArray();
        JsonBuild jb = new JsonBuild();
        Token t = dl.login();

        String annname = "STA-" + System.nanoTime();
        String url = SystemTesting.server + SystemTesting.PORT
                + "/annotation/field";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", t.getToken());

        SystemTesting.addedfields.addanno(annname);

        JsonPrimitive element = new JsonPrimitive("val1");
        ja.add(element);
        jb.property("name", annname).add("type", ja).property("default", "val1").property("forced", false);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(jb.build().toString().getBytes());
        wr.flush();
        wr.close();
        con.getResponseCode();
        testann.getAnnotations();
        if (testann.getResponse().contains(annname) && (con.getResponseCode() == 201)) {
            remove(t, annname);
            return true;
        }
        resultClass.getInstance().addError("Annotations do not exist: " + annname);
        remove(t, annname);

        return false;

    }

    private static void remove(Token t, String annname) throws IOException {
        String url = SystemTesting.server + SystemTesting.PORT
                + "/annotation/field/" + annname;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");
        con.setRequestProperty("Authorization", t.getToken());
    }

    @Override
    public void run() {
        for (int i = 0; i < Values.NLOOPS; i++) {
            try {
                Values.incaddannotot();
                if (sendAddAnnotation()) {
                    Values.incaddannoacc();
                }
            } catch (Exception e) {
                resultClass.getInstance().addError(e.getMessage());
            }
        }
    }
}
