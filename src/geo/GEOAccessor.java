package geo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GEOAccessor{

	public static String getMatrixFileURL(String id) throws MalformedURLException, IOException {
		InputStream is;
		Document doc = null;
		String url = "http://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc="
				+ id + "&form=html";

		is = new URL(url).openStream();
		doc = Jsoup.parse(is, "UTF-8", url);

		Elements links = doc.getElementsByTag("a");
		for (int i = 0; i < links.size(); i++) {

			Element link = links.get(i);
			String linkHref = link.attr("href");
			String linkText = link.text();

			if(linkText.equals("Series Matrix File(s)")) {
				String[] temp = linkHref.split("/");
				String matrixFileURL = linkHref + temp[temp.length - 2].trim() + "_series_matrix.txt.gz";
				return matrixFileURL;
			}
		}
		return null;
	}

	public static void downloadMatrixFile(String matrixFileURL, String tempFileLoc) throws MalformedURLException, IOException {
		InputStream is;

		is = new URL(matrixFileURL).openStream();
		OutputStream outputStream = new FileOutputStream(new File(tempFileLoc));
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = is.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		outputStream.close();
		is.close();

	}

	public static void gunzipFile(String tempZipFileLoc, String tempFileLoc) throws FileNotFoundException, IOException{

		byte[] buffer = new byte[1024];

		GZIPInputStream gzis =
				new GZIPInputStream(new FileInputStream(tempZipFileLoc));

		FileOutputStream out = new FileOutputStream(tempFileLoc);

		int len;
		while ((len = gzis.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}

		gzis.close();
		out.close();

		File file = new File(tempZipFileLoc);
		file.delete();
	}
}
