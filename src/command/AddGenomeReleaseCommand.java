package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

import response.AddGenomeReleaseResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

//TODO: Add validation code on lengths etc.

/**
 * Class used to handle adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommand extends Command {

	@Expose
	private String fileName = null;

	@Expose
	private String specie = null;

	@Expose
	private String genomeVersion = null;

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		if( (fileName == null) || (specie == null) || (genomeVersion == null) ) {

			return false;

		}

		return true;

	}

	/**
	 * method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		Response rsp = null;
		DatabaseAccessor db = null;

		try {

			db = initDB();
			String filePath = db.addGenomeRelease(genomeVersion, specie, fileName);
			rsp = new AddGenomeReleaseResponse(StatusCode.CREATED, filePath);

		} catch (SQLException e) {
			//Takes care of the duplicate key.
			if(e.getErrorCode() == 0) {

				rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

			} else {

				rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);

			}

		} catch (IOException e) {

			rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

		} finally {

			try {

				db.close();

			} catch (SQLException e) {

				rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);

			}

		}

		return rsp;

	}

}