package pvt14servertest;

import java.io.*;

public class ToWeb {

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
        System.out.println("HTML LOG FILE " + (f.canWrite() ? "EXISTS" : "DOES NOT EXIST [testres.html]"));
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
