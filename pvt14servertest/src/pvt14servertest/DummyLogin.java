package pvt14servertest;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;

public class DummyLogin {
    public Token token;

    public Token login() {

        HttpURLConnection con;
        try {
            con = initConnection();
            String json_output = new JsonBuild().property("username", "epicon").
                    property("password","umea@2014").build().toString();
            sendData(con, json_output);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder responseBuffer = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            in.close();
            String response = responseBuffer.toString();

            Gson gson = new Gson();
            token = gson.fromJson(response, Token.class);
        } catch (IOException ignore) {
        }
        return token;
    }

    private HttpURLConnection initConnection() throws IOException {
        String url = SystemTesting.server + SystemTesting.PORT
                + "/login";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        return con;
    }

    private static String randomtext() {
        SecureRandom random = new SecureRandom();
        return new BigInteger((int) (Math.round(Math.random() + 3) * 150),
                random).toString(32);

    }


    // Send json output to con
    private void sendData(URLConnection con, String json_output)
            throws IOException {
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(json_output.getBytes());
        wr.flush();
        wr.close();
    }

}