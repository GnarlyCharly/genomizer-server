package annotations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import pvt14servertest.*;

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
public class TestSendAnnotation extends Thread{

    private static DummyLogin dl = new DummyLogin();
    private static TestAnnotations testann = new TestAnnotations();



    private static boolean sendAddAnnotation() throws Exception {
        String url =  SystemTesting.server + SystemTesting.PORT
                + "/annotation";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", dl.login().getToken());
        con.setRequestProperty("Content-Type", "application/json");

        JsonObject jj=new JsonObject();
        String annname = "systemtestann-"+System.nanoTime();
        SystemTesting.addedfields.addanno(annname);
        jj.addProperty("name", annname);
        JsonArray ja = new JsonArray();
        JsonPrimitive element = new JsonPrimitive("val1");
        ja.add(element);
        jj.add("type", ja);
        jj.addProperty("default", "val1");
        jj.addProperty("forced", false);

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(jj.toString().getBytes());
        wr.flush();
        wr.close();

        if(testann.getResponse().contains(annname))
            return true;
        return false;

//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Response Body: " + printResponse(con));

    }

    @Override
    public void run() {
        for (int i = 0; i < Values.NLOOPS; i++) {
            try {
                Values.incanntot();
                if (sendAddAnnotation()) {
                    Values.incannacc();
                }

            } catch (Exception e) {

                resultClass.getInstance().addError(e.getMessage());
                // System.err.println("ERROR, received: " + response);
                // e.printStackTrace();
            }
        }
    }
}
