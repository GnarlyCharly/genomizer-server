package experiment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import pvt14servertest.*;
import pvt14servertest.AddedFields;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TestSendExperiment extends Thread {

	public static Token token;
	public Token tokporten = null;
	private static ArrayList<String[]> propertyList;

	private DummyLogin logindummy;

	private AddedFields exps;

	private int nloops;

	public TestSendExperiment(int nloops, AddedFields exps) {
		this.exps = exps;
		logindummy = new DummyLogin();

		this.nloops = nloops;
		propertyList = new ArrayList<String[]>();
		// propertyList.add(new String[] { "name", "pubmedID", "value", "123"
		// });
		propertyList.add(new String[] { "name", "Species", "value", "Human" });
		// propertyList.add(new String[] { "name", "genome release", "value",
		// "v.123" });
		// propertyList.add(new String[] { "name", "cell line", "value", "yes"
		// });
		// propertyList.add(new String[] { "name", "development stage", "value",
		// "larva" });
		// propertyList.add(new String[] { "name", "sex", "value", "male" });
		// propertyList.add(new String[] { "name", "tissue", "value", "eye" });
	}

	@Override
	public void run() {
		for (int i = 0; i < nloops; i++) {
			for (String[] props : propertyList) {
				try {
					Values.incsendexptot();
					if (sendAddExperiment(props))
						Values.incsendexpacc();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resultClass.getInstance().addError(e.getMessage());
				}
			}
		}
	}

	private boolean sendAddExperiment(String[] props) throws Exception {

		token = logindummy.login();

		String url = SystemTesting.server + SystemTesting.PORT
				+ "/experiment";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		// add request header
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/jsovaluen");
		JsonObject ja = new JsonObject();

		String expname = "Jonas_Experiment" + System.nanoTime();
		exps.addExps(expname);
		ja.addProperty("name", expname);
		ja.addProperty("createdBy", "jonas");

		JsonArray annotations = new JsonArray();

		JsonObject ann1 = new JsonObject();
		// ann1.addProperty(props[0], props[1]);
		ann1.addProperty("id", "1");
		ann1.addProperty(props[0], props[1]);
		ann1.addProperty(props[2], props[3]);
		annotations.add(ann1);

		ja.add("annotations", annotations);

		String json_output = ja.toString();
		// System.out.println(json_output);

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

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
		return true;

	}

}
