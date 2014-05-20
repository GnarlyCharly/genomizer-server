package experiment;

import pvt14servertest.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestDeleteExperiment extends Thread {

	private static DummyLogin logindummy = new DummyLogin();

	public TestDeleteExperiment() {
	}

    @SuppressWarnings("StringBufferMayBeStringBuilder")
    private static boolean sendDeleteExperiment(String expname)
			throws Exception {
        Token token = logindummy.login();
		URL obj = new URL(SystemTesting.server+ SystemTesting.PORT
				+ "/experiment/" + expname);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("DELETE");
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


		return responseCode == 200;
	}

	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			try {
				Values.incdeleteexptot();
				if (sendDeleteExperiment(SystemTesting.addedfields.getExp())) {
					Values.incdeleteexpacc();
				}
			} catch (Exception e) {
				resultClass.getInstance().addError(e.getMessage());
			}
		}

	}
}
