package transfer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Project: genomizer-Server
 * Package: transfer
 * User: c08esn
 * Date: 4/25/14
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadCommand extends Command {

    private int portNr = 7777;

    private String path;
    private String ipAddress;

    private Socket sendSocket;

    public UploadCommand(String path, String ipAddress) {
        this.ipAddress = ipAddress;
        this.path = path;
    }

    @Override
    public void execute() {
        try {

            sendSocket = new Socket(ipAddress, portNr);

            byte[] sendData = dlLinkJSON().getBytes();
            OutputStream output = sendSocket.getOutputStream();
            output.write(sendData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public String pathToURL() {
        return "POST " + "http://ip/DB/" + path + ":8080" + " HTTP/1.1";
    }

    public String dlLinkJSON() {
        return "200 (OK) \n" +
                "Content-Type: application/json \n\n" +
                "{\n" +
                "upload-link: " + pathToURL() + "\n" +
                "}";
    }
}
