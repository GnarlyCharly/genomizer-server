package pvt14servertest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DummyLogin {
	public Token token;

	public Token login() {

		HttpURLConnection con;
		try {
			con = initConnection();

			// Create the json string output from uname and pass
			String json_output = createJsonString(randomtext(), randomtext());

			// Send json output to con
			sendData(con, json_output);

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
			String response = responseBuffer.toString();

			Gson gson = new Gson();
			token = gson.fromJson(response, Token.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return token;
	}

	private HttpURLConnection initConnection() throws IOException {
		String url = SystemTesting.server +SystemTesting.PORT
				+ "/login";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");
		// add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		return con;
	}

	private static String randomtext() {
		SecureRandom random = new SecureRandom();
		return new BigInteger((int) (Math.round(Math.random() + 3) * 150),
				random).toString(32);

	}

	// Create the json string output from uname and pass
	private String createJsonString(String uname, String pass) {
		JsonObject jj = new JsonObject();
		// SEND RANDOM DATA LOGIN
		jj.addProperty("username", uname);
		jj.addProperty("password", pass);
		return jj.toString();
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
