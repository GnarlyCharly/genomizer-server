package geo;

import java.io.IOException;

import response.Response;

import com.google.gson.annotations.Expose;
import command.Command;

public class GEODownloadFileCommand extends Command{

	public GEODownloadFileCommand() {

	}

	@Expose
	private String experimentID;

	@Expose
	private String fileName;

	@Expose
 	private String type;

	@Expose
	private String metaData;

	@Expose
	private String author;

	@Expose
	private String uploader;

	@Expose
	private boolean isPrivate;

	@Expose
	private String grVersion;

	@Expose
	private String url;

	/**
	 * Validates the request by checking
	 * the attributes. No attribute can be null
	 * and type needs to be either "raw", "profile",
	 * or "region".
	 */
	@Override
	public boolean validate() {

		if(experimentID == null || fileName == null || type == null || url == null) {
			return false;
		}
		return true;
	}


	@Override
	public Response execute() {
//		DatabaseAccessor dbaccessor = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "database");
		int filetype;
		switch (type) {
		case "raw":
			filetype = 1;
			break;
		case "profile":
			filetype = 2;
			break;
		case "region":
			filetype = 3;
			break;
		case "other":
			filetype = 4;
			break;
		}

		try {
			String sraURL = url;
			int index;
			String theFileName;
			if(url.endsWith("/")) {
				sraURL = GEOFileDownloader.getSRAFromDir("ftp://ftp-trace.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByExp/sra/SRX%2FSRX287%2FSRX287596/");
				index = sraURL.lastIndexOf('/');
				theFileName = sraURL.substring(index + 1);
				index = theFileName.lastIndexOf('.');
				theFileName = theFileName.substring(0, index) + ".fastq";
			} else {
				index = sraURL.lastIndexOf('/');
				theFileName = sraURL.substring(index);
			}


//			FileTuple filetuple = dbaccessor.addNewFile(experimentID, filetype, theFileName, null, metaData, author, uploader, isPrivate, grVersion);
//			String path = filetuple.path;


			GEOFileDownloader.downloadFile(sraURL, "/var/www/test/");
//			GEOFileDownloader.downloadFileDirect(sraURL, "/var/www/test", theFileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		GEODownloadFileCommand geo = new GEODownloadFileCommand();

		geo.execute();
	}

}
