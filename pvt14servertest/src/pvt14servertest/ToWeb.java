package pvt14servertest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ToWeb {

	Writer writer = null;
	String starthtml = "<html><head>";
	String endhtml = "</body></html>";
	String css = "<style media=\"screen\" type=\"text/css\">"
			+ "*{font-family:verdana;" + "background:#FEFEFF}" + "progress {"
			+ "width: 20em;" + "height: 1.8em;" + "background: #FF180B;}"
			+ "progress::-moz-progress-bar {background-color:#1CCC15;}"
			+ "</style></head><body>";
	Writer w;

	public ToWeb() throws FileNotFoundException {
		File f = new File(SystemTesting.htmlfile);
		System.out.println("file can be writen to: " + f.canWrite());
		FileOutputStream is;
		is = new FileOutputStream(f);

		OutputStreamWriter osw = new OutputStreamWriter(is);
		w = new BufferedWriter(osw);
	}

	public void starthtml() throws IOException {
		w.write(starthtml + css);
	}

	public void createTestSection(String testType) throws IOException {
		w.write("<br><b>" + testType + "</b><br>");
	}

	public void writeToHTML(int percent) throws IOException {
		w.write("<progress value=\"" + percent + "\" max=\"" + 100
				+ "\"></progress>" + "<span class=\"percentage\">" + percent
				+ "%  Successful</span>");
	}

	public void endTest() throws IOException {
		w.write("<br><hr>");
	}

	public void endhtml() throws IOException {
		w.write(endhtml);
		w.close();
	}

}
