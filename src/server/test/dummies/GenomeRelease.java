package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

public class GenomeRelease {

	public GenomeRelease() {
		// TODO Auto-generated constructor stub
	}

	static void sendGetGenomeRelease() throws Exception {

		URL obj = new URL(testSettings.url + "/genomeRelease");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", Login.getToken());


		System.out.println("\nSending 'GET' request to URL : " + testSettings.url);
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

	static void sendGetGenomeReleaseSpecies(String specie) throws Exception {
		URL obj = new URL(testSettings.url + "/genomeRelease/" + specie);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", Login.getToken());


		System.out.println("\nSending 'GET' request to URL : " + testSettings.url);
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

	static void sendAddGenomeRelease() throws Exception {
		URL obj = new URL(testSettings.url + "/genomeRelease/");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", Login.getToken());


		System.out.println("\nSending 'GET' request to URL : " + testSettings.url);
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

}