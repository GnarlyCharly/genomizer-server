package loginlogout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;

import pvt14servertest.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TestLogin extends Thread {
	public static Token token;
	public Token tokporten = null;

	public int tok = 0;

	public String[] corrToken = "3d5f2eed-6dcf-48a4-906e-1643d54868bb"
			.split("-");

	public TestLogin() {

	}

	/**
	 * Sends a login request
	 * 
	 * @param uname
	 * @param pass
	 * @return
	 * @throws Exception
	 */
	public boolean sendLogin(String uname, String pass) throws Exception {
		try {

			HttpURLConnection con = initConnection();

			// Create the json string output from uname and pass
			String json_output =   new JsonBuild().property("username", uname).property("password", pass).build().toString();

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

			// System.out.println(token.getToken());

			if (token.getToken() != null || token.getToken() != ""
					&& responseCode == 200) {
				for (int i = 0; i < 5; i++) {
					if (token.getToken().split("-")[i].length() != corrToken[i]
							.length())
						return false;
				}
				return true;
			}

		} catch (MalformedURLException e) {
		}
		return false;

	}

	// Init the http connection
	private HttpURLConnection initConnection() throws IOException {

        String url = SystemTesting.server+ SystemTesting.PORT + "/login";
        URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");
		// add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		return con;
	}

	// Create the json string output from uname and pass


        // SEND RANDOM DATA LOGIN



	// Send json output to con
	private void sendData(URLConnection con, String json_output)
			throws IOException {
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();
	}

	private static String randomtext() {
		SecureRandom random = new SecureRandom();
		return new BigInteger((int) (Math.round(Math.random() + 3) * 150),
				random).toString(32);

	}

	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			try {
				Values.inclogintot();
				if (sendLogin(randomtext(), randomtext())) {
					Values.incloginacc();
				}

			} catch (Exception e) {

				// if (e.getMessage().compareTo("Stream closed") == 0)
				// SystemTesting.incloginacc();
				// e.printStackTrace();

				resultClass.getInstance().addError(e.getMessage());
			}
		}
	}
}
