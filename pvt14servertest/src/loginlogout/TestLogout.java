package loginlogout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pvt14servertest.DummyLogin;
import pvt14servertest.SystemTesting;
import pvt14servertest.Token;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class TestLogout extends Thread {

	private static Token token;
	private static DummyLogin dl;
	private static int responseCode;

	public TestLogout() {
		dl = new DummyLogin();
	}

	private static boolean sendLogout() throws Exception {
		token = dl.login();

		String url = SystemTesting.server+ SystemTesting.PORT
				+ "/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("DELETE");

		// add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		responseCode = con.getResponseCode();
		// System.out.print(responseCode + "  ");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));

		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();
		if (responseCode == 200)
			return true;

		return false;

	}

	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			try {
				Values.inclogouttot();
				if (sendLogout()) {
					Values.inclogoutacc();
				}

			} catch (Exception e) {

				// e.printStackTrace();

				resultClass.getInstance().addError(
						e.getMessage() + " Error:" + responseCode);
			}
		}
	}

}
