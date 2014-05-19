package annotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pvt14servertest.SystemTesting;
import pvt14servertest.Token;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class TestAnnotations implements Runnable {

	public static Token token;
	private static int nloops;

	private static BufferedReader in;


    private static String response;

	public TestAnnotations() {
		TestAnnotations.nloops = nloops;
		token = new Token("a01c9b9d-283a-4bcc-b0ee-96f7c9cef4fd");

	}

    public static String getResponse() {
        return response;
    }


    private boolean getAnnotations() throws Exception {

		HttpURLConnection con = initConnection();

		int responseCode = con.getResponseCode();
		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		try {
			in.close();
		} catch (IOException e) {

		}
		response = responseBuffer.toString();

		if ((responseCode == 200) && (response != null || response != ""))
			return true;

		return false;

	}

	// Init the http connection
	private HttpURLConnection initConnection() throws IOException {
		String url = SystemTesting.server+ SystemTesting.PORT
				+ "/annotation";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		// con.setRequestProperty("Content-Length",
		// String.valueOf(jj.toString().getBytes().length));
		return con;
	}

	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			try {
				Values.incanntot();
				if (getAnnotations()) {
					Values.incannacc();
				}

			} catch (Exception e) {

				resultClass.getInstance().addError(e.getMessage());
				// System.err.println("ERROR, received: " + response);
				// e.printStackTrace();
			}
		}
	}
}
