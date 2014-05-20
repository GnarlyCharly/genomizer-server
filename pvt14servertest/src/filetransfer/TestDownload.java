package filetransfer;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project: genomizer-Server
 * Package: filetransfer
 * User: c08esn
 * Date: 5/20/14
 * Time: 8:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestDownload extends Thread {


    public TestDownload() {

    }

    public boolean sendDownloadRequest(String url) throws IOException {
        String inputLine;
        String response;

        File savefile = new File( System.getProperty( "user.home" )+"/testfile.txt");
        StringBuilder responseBuffer = new StringBuilder();

        URL obj = new URL(url.replaceAll("\\\\u003d", "="));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestMethod("GET");
        FileOutputStream fos = new FileOutputStream(savefile);
        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        while ((inputLine = in.readLine()) != null) {
            fos.write(inputLine.getBytes());
            fos.write("\n".getBytes());
            responseBuffer.append(inputLine);
            responseBuffer.append("\n");
        }
        in.close();
        fos.close();
        response = responseBuffer.toString();
        return responseCode==200 && !response.equals("");
    }


    @Override
    public void run() {
        int loops;
        if (SystemTesting.addedfields.geturlsize() <= Values.NLOOPS) {
            loops = SystemTesting.addedfields.geturlsize();
        } else {
            loops = Values.NLOOPS;
        }

        for (int i = 0; i < loops; i++) {
            try {
                Values.incdlfiletot();
                if (sendDownloadRequest(SystemTesting.addedfields.getfileurl())) {
                    Values.incdlfileacc();
                }
            } catch (Exception e) {
                resultClass.getInstance().addError(e.getMessage());
            }
        }
    }

}
