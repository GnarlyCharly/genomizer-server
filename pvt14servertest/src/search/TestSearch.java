package search;

import pvt14servertest.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TestSearch extends Thread {
	private static DummyLogin dl = new DummyLogin();
    private static ArrayList<String> urls = new ArrayList<String>();

	public TestSearch() {

	}

	public static boolean sendSearchRequest() throws Exception {
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
		StringBuilder responseBuffer = new StringBuilder();

        String paths="";
		while ((inputLine = in.readLine()) != null) {
            if(inputLine.contains("url")){
                paths+=inputLine+"\n";
            }
			responseBuffer.append(inputLine);
			responseBuffer.append("\n");
		}
		in.close();
        String response = responseBuffer.toString();
        extractUrls(paths.split("\""));

        return responseCode == 200 && response.contains("annotations");
    }



    private static ArrayList<String> extractUrls(String[] a){
        for(String val: a){
            if(val.contains("http")){
                SystemTesting.addedfields.addfileurls(val);
                urls.add(val);
            }
        }
        return urls;
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
