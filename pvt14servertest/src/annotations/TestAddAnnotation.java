package annotations;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
public class TestAddAnnotation extends Thread{

    private static DummyLogin dl = new DummyLogin();
    private static TestAnnotations testann;

    public TestAddAnnotation(){

    }



    private static boolean sendAddAnnotation() throws Exception {
        testann = new TestAnnotations();

        Token t = dl.login();
        String annname = "STA-"+System.nanoTime();
        String url =  SystemTesting.server + SystemTesting.PORT
                + "/annotation/field";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", t.getToken());

        JsonObject jj=new JsonObject();

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
        con.getResponseCode();



        //TEST
        testann.getAnnotations();
//        System.out.println(con.getResponseCode()+ " " + testann.getResponse() );
        if(testann.getResponse().contains(annname) && (con.getResponseCode()==201)) {
            remove(t, annname);
            return true;
        }
        resultClass.getInstance().addError("Annotations do not exist: " +annname);
        remove(t, annname);

        return false;

//        System.out.println("\nSending 'POST' request to URL : " + url);
//        System.out.println("Response Body: " + printResponse(con));

    }

    private static void remove(Token t, String annname) throws IOException {
        String url;
        URL obj;
        HttpURLConnection con;//TEST
        url = SystemTesting.server+ SystemTesting.PORT
                + "/annotation/field/"+annname;
        obj = new URL(url);
        con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("DELETE");
//        con.setRequestProperty("Content-Type", "application/json");
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
                // System.err.println("ERROR, received: " + response);
                // e.printStackTrace();
            }
        }
    }
}
