package geo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import response.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import command.Command;

public class GEOCommand extends Command {

	private String body;

	public GEOCommand(String body) {
		this.body = body;
	}

	@Override
	public boolean validate() {
		try {
			String fileID = body.substring(11, body.length() - 2);
			String matrixFileURL = GEOAccessor.getMatrixFileURL(fileID);
			new URL(matrixFileURL).openStream();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public Response execute() {

		String fileID = body.substring(11, body.length() - 2);
		long time = System.nanoTime();

		// "/home/pvt/tempfiles/Matrixfile" + System.nanoTime() + ".txt.gz"
		String tempZipFileLoc = "/home/dv12/dv12tkn/MatrxiFile" + time
				+ ".txt.gz";
		// "/home/pvt/tempfiles/Matrixfile" + System.nanoTime() + ".txt"
		String tempFileLoc = "/home/dv12/dv12tkn/MatrxiFile" + time + ".txt";
		ArrayList<GEOFileTuple> infoList = null;

		GetGEOResponse geoResponse = null;

		try {
			String matrixFileURL = GEOAccessor.getMatrixFileURL(fileID);
			GEOAccessor.downloadMatrixFile(matrixFileURL, tempZipFileLoc);

			GEOAccessor.gunzipFile(tempZipFileLoc, tempFileLoc);

			infoList = TXTParser.readFile(tempFileLoc);
			File file = new File(tempFileLoc);
			file.delete();
			geoResponse = new GetGEOResponse(infoList, 200);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return geoResponse;
	}

	public static String toPrettyFormat(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonString).getAsJsonObject();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		return prettyJson;
	}

	public static void main(String[] args) {
		GEOCommand geo = new GEOCommand("GSE47236");
		String temp = geo.execute().getBody();

		System.out.println(toPrettyFormat(temp));
	}
}
