package annotations;

import pvt14servertest.SystemTesting;
import pvt14servertest.Token;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class TestAnnotations implements Runnable {

    public static Token token;

    private static BufferedReader in;


    private volatile static String response = "";


    private static int responseCode;

    public TestAnnotations() {
        token = new Token("a01c9b9d-283a-4bcc-b0ee-96f7c9cef4fd");

    }

    public synchronized static String getResponse() {
        return response;
    }


    @SuppressWarnings({"EmptyCatchBlock", "StringBufferMayBeStringBuilder"})
    public boolean getAnnotations() throws IOException {

        HttpURLConnection con = initConnection();

        responseCode = con.getResponseCode();

        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer responseBuffer = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            responseBuffer.append(inputLine);
        }
        in.close();

        response = responseBuffer.toString();

        response = response.replaceAll(",","");
        //needed to find out how many notations there is.
        String[] split1 = response.split("\\}");
        String[] split2 = response.split("\\}",split1.length-1);

        int nsucc=0;
        for(String annot : split2){
           if(annot.contains("name") && annot.contains("values") && annot.contains("forced"))
               nsucc++;
        }



        return ((responseCode == 200) && (response != null || response.equals("")) && (nsucc == split2.length));


    }

    // Init the http connection
    private HttpURLConnection initConnection() throws IOException {
        String url = SystemTesting.server + SystemTesting.PORT
                + "/annotation";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", token.getToken());

        return con;
    }

    @Override
    public void run() {
        for (int i = 0; i < Values.NLOOPS; i++) {
            try {
                Values.incanntot();
                if (getAnnotations()) {
                    Values.incannacc();
                } else{
                    resultClass.getInstance().addError("Annotation syntax incorrect");
                }
            } catch (Exception e) {
                resultClass.getInstance().addError(e.getMessage());
            }
        }
    }
}
