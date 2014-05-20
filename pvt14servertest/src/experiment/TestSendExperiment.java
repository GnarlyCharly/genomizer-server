package experiment;

import com.google.gson.JsonArray;
import pvt14servertest.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TestSendExperiment extends Thread {

	public static Token token;
	private static ArrayList<String[]> propertyList;

	private DummyLogin logindummy;




	public TestSendExperiment() {
		logindummy = new DummyLogin();

		propertyList = new ArrayList<String[]>();
		propertyList.add(new String[] { "name", "Species", "value", "Human" });
	}

	@Override
	public void run() {
		for (int i = 0; i < Values.NLOOPS; i++) {
			for (String[] props : propertyList) {
				try {
					Values.incsendexptot();
					if (sendAddExperiment(props))
						Values.incsendexpacc();
				} catch (Exception e) {
                    e.printStackTrace();
					resultClass.getInstance().addError(e.getMessage());
				}
			}
		}
	}

    private boolean sendAddExperiment(String[] props) throws Exception {
        String inputLine;
		token = logindummy.login();
        JsonArray annotations = new JsonArray();
        StringBuilder responseBuffer = new StringBuilder();

		String url = SystemTesting.server + SystemTesting.PORT
				+ "/experiment";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);


        con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");
        String expname = "ST-" + System.nanoTime();
        SystemTesting.addedfields.addExps(expname);

        annotations.add(new JsonBuild().property("id","1")
                .property(props[0], props[1]).property(props[2], props[3]).build());
        String json_output = new JsonBuild().property("name", expname)
                .property("createdBy", "SystemTest").add("annotations", annotations)
                .build().toString();

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		return (responseCode==200);
	}

}
