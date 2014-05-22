package loginlogout;

import com.google.gson.Gson;
import pvt14servertest.*;

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

public class TestLogin extends Thread {
	public static Token token;

	public String[] corrToken = "3d5f2eed-6dcf-48a4-906e-1643d54868bb"
			.split("-");

	public TestLogin() {

	}

	@SuppressWarnings("ConstantConditions")
    public boolean sendLogin(String uname, String pass) throws IOException, NullPointerException {
		try {

			HttpURLConnection con = initConnection();
			String json_output =   new JsonBuild().property("username", "epicon").
                    property("password", "umea@2014").build().toString();
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
			String response = responseBuffer.toString();

			Gson gson = new Gson();
			token = gson.fromJson(response, Token.class);
			if (token.getToken() != null || !token.getToken().equals("")
					&& responseCode == 200) {
				for (int i = 0; i < 5; i++) {
					if (token.getToken().split("-")[i].length() != corrToken[i]
							.length())
						return false;
				}
				return true;
			}

		} catch (MalformedURLException ignored) {
		}

		return false;

	}

	private HttpURLConnection initConnection() throws IOException {

        String url = SystemTesting.server+ SystemTesting.PORT + "/login";
        URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setDoOutput(true);

		return con;
	}

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
