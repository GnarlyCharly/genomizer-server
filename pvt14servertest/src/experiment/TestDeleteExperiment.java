package experiment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pvt14servertest.*;
import pvt14servertest.AddedFields;

public class TestDeleteExperiment extends Thread {

	private static DummyLogin logindummy = new DummyLogin();
	private static AddedFields exps;
	private static int nloops;
	private static Token token;

	public TestDeleteExperiment(int nloops, AddedFields exps) {
		this.exps = exps;
		this.nloops = nloops;
	}

	private static boolean sendDeleteExperiment(String expname)
			throws Exception {
		token = logindummy.login();
		URL obj = new URL(SystemTesting.server+ SystemTesting.PORT
				+ "/experiment/" + expname);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("DELETE");
		// add request header
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");
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

		if (responseCode == 200) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		for (int i = 0; i < nloops; i++) {
			try {
				Values.incdeleteexptot();
				if (sendDeleteExperiment(exps.getExp())) {
					Values.incdeleteexpacc();
				}
			} catch (Exception e) {
				resultClass.getInstance().addError(e.getMessage());
			}
		}

	}
}
