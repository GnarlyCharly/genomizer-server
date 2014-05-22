package transfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

public class TransferData {

	public TransferData() {
	}

	/**
	 * Parses data from a multipart/form-data and saves the content to a file.
	 * @param inputStream The stream containing the body of the multipart/form-data.
	 * @param exchange HttpExchange object.
	 * @return A string with the response code.
	 */
	public String parseFileFromBody(InputStream inputStream, HttpExchange exchange) {

		String filePath = exchange.getRequestURI().toString().split("=")[1];

		System.out.println("filepath = " + filePath);
		String length = exchange.getRequestHeaders().getFirst("Content-length");
		BufferedInputStream bis = new BufferedInputStream(inputStream);

		String body = null;
		byte[] byteRead = new byte[1];
		byte[] byteArr = new byte[1024];
		byte[] fileArr = null;
		int i = 0;
		int nrOfBytes = 0;
		boolean first = true;

		File file = new File(filePath);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			body = "400";
			return body;
		}

		try {
			while(bis.read(byteRead, 0, 1) != -1) {
				if(byteRead[0] == '\r') {

					String line = new String(byteArr);
					byteArr = new byte[1024];

					if(line.indexOf("Content-") == -1 && !first) {
						i = 0;
						bis.read(byteRead, 0, 1);

						int len = Integer.parseInt(length) - nrOfBytes;

						int bytes = 4096;
						fileArr = new byte[bytes];

						while(len > 0) {;
							if(len > bytes) {
								fileArr = new byte[bytes];
								int temp = 0;
								int tempBytes = bytes;
								//Makes sure 'bytes' number of bytes are read.
								while((temp = bis.read(fileArr, temp, tempBytes)) != tempBytes) {
									tempBytes -= temp;
								}
								len -= bytes;

							} else {
								fileArr = new byte[len];
								bis.read(fileArr, 0, len);
								len = -1;
							}

							try {
								fos.write(fileArr);
								fos.flush();
							} catch (FileNotFoundException e) {
								body = "404";
								fos.close();
								return body;
							} catch (IOException e) {
								body = "400";
								fos.close();
								return body;
							}
						}
						fos.close();
						break;
					} else {
						if(first) {
							nrOfBytes += (nrOfBytes + 8);
						}
						i = 0;
						first = false;
						nrOfBytes++;
					}
				} else {
					byteArr[i++] = byteRead[0];
					nrOfBytes++;
				}
			}

			body = new String(fileArr);

		} catch (IOException e) {
			body = "400";
			return body;
		}

		body = "200";

		return body;
	}

	public void sendFile(FileInputStream fis, OutputStream os) throws IOException {
		if(fis != null) {
			byte[] buffer = new byte[1024];
		    int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1)
			{
			    os.write(buffer, 0, bytesRead);
			}
		}
	}
}