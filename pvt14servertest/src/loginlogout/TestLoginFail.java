package loginlogout;

import com.google.gson.Gson;
import pvt14servertest.SystemTesting;
import pvt14servertest.Token;
import pvt14servertest.Values;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TestLoginFail extends Thread {

    public TestLoginFail() {

	}

	@SuppressWarnings("ConstantConditions")
    public boolean sendLogin() throws Exception {
		String response;
        Gson gson;
        Token token;

		HttpURLConnection con = initConnection();
		String json_output = "";// createJsonString(uname, pass);
		sendData(con, json_output);

		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuilder responseBuffer = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
        response = responseBuffer.toString();
        gson = new Gson();
        token = gson.fromJson(response, Token.class);

        return !(token.getToken() != null || !token.getToken().equals("")
				&& responseCode == 200);


	}

	// Init the http connection
	private HttpURLConnection initConnection() throws IOException {
		String url = SystemTesting.server+ SystemTesting.PORT
				+ "/login";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);
		return con;
	}


	// Send json output to con
	private void sendData(URLConnection con, String json_output)
			throws IOException {
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();
	}


	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			try {
				Values.incloginfailtot();
				if (sendLogin()) {
					Values.incloginfailfails();
				}

			} catch (Exception e) {
				Values.incloginfailfails();
			}
		}
	}

}
