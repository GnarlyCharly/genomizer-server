package geo;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import response.Response;

public class GetGEOResponse extends Response {

	private String body;

	public GetGEOResponse(ArrayList<GEOFileTuple> geoFileList, int code) {
		if (geoFileList != null) {
			this.code = code;
			JsonObject obj = new JsonObject();

			JsonArray geofileArray = new JsonArray();
			JsonObject geoSharedInfo = new JsonObject();
			for (int i = 0; i < geoFileList.size(); i++) {
				GEOFileTuple geoft = geoFileList.get(i);
				GsonBuilder gsonBuilder = new GsonBuilder();
				Gson gson = gsonBuilder.setPrettyPrinting().create();
				if (i == 0) {
					geoSharedInfo = gson.toJsonTree(geoft).getAsJsonObject();
					// System.out.println(geoSharedInfo.toString());
				} else {
					JsonElement geofileJson = gson.toJsonTree(geoft);
					// System.out.println(geofileJson.toString());

					geofileArray.add(geofileJson);
				}
			}

			obj.add("shared_info", geoSharedInfo);
			obj.add("files", geofileArray);

			body = obj.toString();
		}
	}

	@Override
	public String getBody() {
		return body;
	}
}
