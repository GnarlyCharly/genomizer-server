package geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class GEOFileDownloader {

	public static String getSRAFromDir(String url) throws IOException {
		url = url + "/";
		// This will get the subfolders from the url
		InputStream is = new URL(url).openStream();
		byte[] buffer = new byte[1024];
		int bytesRead = -1;
		StringBuilder page = new StringBuilder(1024);
		while ((bytesRead = is.read(buffer)) != -1) {
			page.append(new String(buffer, 0, bytesRead));
		}
		String fileInfo = page.toString();

		// Pick out the last word in fileInfo, that is the filename that we want
		String[] temp = fileInfo.split(" ");
		// Then paste the string together
		url = url + temp[temp.length - 1].trim() + "/"
				+ temp[temp.length - 1].trim() + ".sra";

		is.close();

		return url;
	}

	public static void downloadFile(String url, String tempFileLoc)
			throws MalformedURLException, IOException {

		// new HttpClient
		HttpClientBuilder hcBuilder = HttpClients.custom();
		CloseableHttpClient httpClient = hcBuilder.build();

		// post header
		HttpPost httpPost = new HttpPost(
				"scratchy.cs.umu.se:8000/download_from_geo.php?url=" + url
						+ "&path=" + tempFileLoc);

		try {
			HttpResponse response;
			// execute HTTP post request
			response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			System.out.println("Response code: "
					+ response.getStatusLine().getStatusCode());
			if (resEntity != null) {

				String responseStr = EntityUtils.toString(resEntity).trim();
				System.out.println("Response: " + responseStr);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void downloadFileDirect(String url, String filePath,
			String fileName) throws IOException, InterruptedException {
		URL urlObject = new URL(url);
		File f = new File(filePath);
		// Create the file
		f.createNewFile();
		// Download the file directly to the file
		ReadableByteChannel rbc = Channels.newChannel(urlObject.openStream());
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();

		// Try to convert the .sra file to a .fastq file with SRA-Toolkit
		Process p = Runtime.getRuntime().exec(
				"fastq-dump " + filePath + " -O " + filePath);
		p.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	public static void main(String[] args) {
		try {
			downloadFileDirect("ftp://ftp-trace.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByExp/sra/SRX%2FSRX287%2FSRX287594/SRR869737/SRR869737.sra",
					"/home/dv12/dv12tkn/Downloads/SRR869737.sra", "SRR869737.sra");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
