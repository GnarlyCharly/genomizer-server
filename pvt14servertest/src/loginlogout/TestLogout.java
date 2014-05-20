package loginlogout;

import pvt14servertest.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestLogout extends Thread {

    private static DummyLogin dl;
	private static int responseCode;

	public TestLogout() {
		dl = new DummyLogin();
	}

	@SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    private static boolean sendLogout() throws Exception {
        Token token = dl.login();
		String url = SystemTesting.server+ SystemTesting.PORT
				+ "/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuilder responseBuffer = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
        return responseCode == 200;

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
				resultClass.getInstance().addError(
						e.getMessage() + " Error:" + responseCode);
			}
		}
	}

}
