package search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import pvt14servertest.DummyLogin;
import pvt14servertest.SystemTesting;
import pvt14servertest.Token;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class TestSearch extends Thread {
	private static DummyLogin dl = new DummyLogin();

	public TestSearch() {

	}

	private static boolean sendSearchRequest() throws Exception {
		Token t = dl.login();
		String query = URLEncoder.encode("Exp1[ExpID]", "UTF-8");
		String url = SystemTesting.server + SystemTesting.PORT
				+ "/search/?annotations=" + query;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", t.getToken());
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
			responseBuffer.append("\n");
		}
		in.close();
		String response = responseBuffer.toString();
		if (responseCode == 200 && response.contains("annotations"))
			return true;
		return false;
	}

	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			try {
				Values.incsearchtot();
				if (sendSearchRequest()) {
					Values.incsearchacc();
				}
			} catch (Exception e) {
				resultClass.getInstance().addError(e.getMessage());
			}
		}
	}
}
