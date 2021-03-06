package database.subClasses;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.FilePathGenerator;
import database.FileValidator;
import database.containers.Experiment;
import database.containers.FileTuple;

/**
 * Class that contains all the methods for adding,changing, getting and removing
 * Files in the database. This class is a subClass of databaseAcessor.java.
 *
 * date: 2014-05-14 version: 1.0
 */
public class FileMethods {

	private Connection conn;
	private FilePathGenerator fpg;
	private ExperimentMethods expMethods;

	/**
	 * Constructor for the fileMethod object.
	 *
	 * @param connection
	 *            Connection, the connection to the database.
	 */
	public FileMethods(Connection connection, FilePathGenerator filePG,
			ExperimentMethods expMethods) {

		conn = connection;
		fpg = filePG;
		this.expMethods = expMethods;
	}

	/**
	 * @param expID
	 *            String The unique name of the experiment. OBS! If not null,
	 *            this must reference an experiment that has been previously
	 *            added.
	 * @param fileType
	 *            int An Integer identifying the file type eg. FileTuple.RAW
	 * @param fileName
	 *            String
	 * @param inputFileName
	 *            String The name of the corresponding input file or null if
	 *            there is no corresponding input file
	 * @param metaData
	 *            String The parameters used in file creation or null if not
	 *            applicable
	 * @param author
	 *            String
	 * @param uploader
	 *            String
	 * @param isPrivate
	 *            boolean
	 * @param genomeRelease
	 *            String The genome release version identifyer (eg. "hg38") or
	 *            null if not applicable. OBS! If not null, this must reference
	 *            a genome release that has been previously uploaded.
	 * @return FileTuple - The FileTuple inserted in the database or null if no
	 *         file was entered into the database.
	 * @throws SQLException
	 *             If the query could not be executed. Possible reasons:
	 *             Duplicate file, Does not reference a valid GenomeRelease.
	 * @throws IOException
	 *             If the experiment does not exist.
	 */
	public FileTuple addNewFile(String expID, int fileType, String fileName,
			String inputFileName, String metaData, String author,
			String uploader, boolean isPrivate, String genomeRelease)
			throws SQLException, IOException {

		if (!FileValidator.fileNameCheck(fileName)) {
			throw new IOException("Invalid filename");
		}

		if (inputFileName != null) {
			if (!FileValidator.fileNameCheck(inputFileName)) {
				throw new IOException("Invalid input filename");
			}
		}

		Experiment e = expMethods.getExperiment(expID);

		if (e == null) {
			throw new IOException("The experiment " + expID
					+ " does not exist!");
		}
		expID = e.getID(); // Correct expID for in case sensitivity

		if (fileType == FileTuple.RAW && e.getNrRawFiles() >= 2) {
			throw new IOException(
					"There are already two raw files for this experiment!");
		}

		FileTuple ft = getProfile(e, metaData);
		String path;
		if (ft == null) {
			path = fpg.generateFilePath(expID, fileType, fileName);
		} else {
			path = ft.getParentFolder() + fileName;
			File profileToAdd = new File(path);
			if (profileToAdd.exists()) {
				throw new IOException(fileName + " with the parameters "
						+ metaData + " already exists!");
			}
		}

		String inputFilePath = null;
		if (inputFileName != null) {
			inputFilePath = getParentFolder(path) + inputFileName;
		}

		String query = "INSERT INTO File "
				+ "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
				+ "Author, Uploader, IsPrivate, ExpID, GRVersion) "
				+ "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, path);

		switch (fileType) {
		case FileTuple.RAW:
			stmt.setString(2, "Raw");
			genomeRelease = null;
			break;
		case FileTuple.PROFILE:
			stmt.setString(2, "Profile");
			break;
		case FileTuple.REGION:
			stmt.setString(2, "Region");
			break;
		default:
			stmt.setString(2, "Other");
			break;
		}

		stmt.setString(3, fileName);
		stmt.setString(4, metaData);
		stmt.setString(5, inputFilePath);
		stmt.setString(6, author);
		stmt.setString(7, uploader);
		stmt.setBoolean(8, isPrivate);
		stmt.setString(9, expID);
		stmt.setString(10, genomeRelease);

		stmt.executeUpdate();
		stmt.close();

		return getFileTuple(path);
	}

	private FileTuple getProfile(Experiment e, String metaData) {

        for (FileTuple ft : e.getFiles()) {

            if (ft.type.equalsIgnoreCase("profile")) {

                if (metaData == null && ft.metaData == null) {
                    return ft;
                }

                if (ft.metaData != null) {
                    if (ft.type.equalsIgnoreCase("profile")
                            && ft.metaData.equals(metaData)) {
                        return ft;
                    }
                }
            }

        }
        return null;
    }

	private String getParentFolder(String filePath) {

		int filenameIndex = filePath.lastIndexOf(File.separator);
		return filePath.substring(0, filenameIndex + 1);
	}

	private String getFileName(String filePath) {

		int filenameIndex = filePath.lastIndexOf(File.separator);
		return filePath.substring(filenameIndex + 1);
	}

	/**
	 * Returns the FileTuple object associated with the given filePath.
	 *
	 * @param filePath
	 *            String
	 * @return FileTuple - The corresponding FileTuple or null if no such file
	 *         exists
	 * @throws SQLException
	 *             If the query could not be executed.
	 */
	public FileTuple getFileTuple(String filePath) throws SQLException {

		String query = "SELECT * FROM File WHERE Path = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, filePath);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			FileTuple fileTuple = new FileTuple(rs);
			stmt.close();
			return fileTuple;
		}
		stmt.close();
		return null;
	}

	/**
	 * Returns the FileTuple object associated with the given fileID.
	 *
	 * @param fileID
	 *            int
	 * @return FileTuple - The corresponding FileTuple or null if no such file
	 *         exists
	 * @throws SQLException
	 *             If the query could not be executed.
	 */
	public FileTuple getFileTuple(int fileID) throws SQLException {

		String query = "SELECT * FROM File WHERE FileID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, fileID);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			FileTuple fileTuple = new FileTuple(rs);
			stmt.close();
			return fileTuple;
		}
		stmt.close();
		return null;
	}

	/**
	 * Deletes a file from the database and the disk. Should throw an
	 * IOException if the method failed to delete the file from disk.
	 *
	 * @param path
	 *            String - the path to the file.
	 * @return int - the number of deleted tuples in the database.
	 * @throws SQLException
	 *             if the query does not succeed
	 * @throws IOException
	 */
	public int deleteFile(String path) throws SQLException, IOException {

		FileTuple ft = getFileTuple(path);

		if (ft == null) {
			throw new IOException("Could not find file at path " + path);
		}

		File fileToDelete = new File(path);
		if (fileToDelete.exists()) {
			fileToDelete.delete();
		}

		File parentFolder = new File(ft.getParentFolder());
		if (ft.type.equalsIgnoreCase("profile") && isEmptyFolder(parentFolder)) {
			parentFolder.delete();
		}

		String statementStr = "DELETE FROM File " + "WHERE (Path ~~* ?)";
		PreparedStatement deleteFile = conn.prepareStatement(statementStr);
		deleteFile.setString(1, path);
		int resCount = deleteFile.executeUpdate();
		deleteFile.close();

		return resCount;
	}

	/**
	 * Deletes a file from the database and the disk using the fileID. Should
	 * throw an IOException if the method failed to delete the file from disk.
	 *
	 * @param fileID
	 *            int - the fileID of the file to be deleted.
	 * @return 1 if deletion was successful, else 0.
	 * @throws SQLException
	 *             If the database could not be contacted
	 * @throws IOException
	 *             If the FileID does not exist in the database
	 */
	public int deleteFile(int fileID) throws SQLException, IOException {

		FileTuple ft = getFileTuple(fileID);

		if (ft == null) {
			throw new IOException("Could not find file with ID " + fileID);
		}

		File fileToDelete = new File(ft.path);

		if (fileToDelete.exists()) {
			fileToDelete.delete();
		}

		File parentFolder = new File(ft.getParentFolder());
		if (ft.type.equalsIgnoreCase("profile") && isEmptyFolder(parentFolder)) {
			parentFolder.delete();
		}

		String query = "DELETE FROM File " + "WHERE FileID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, fileID);
		int res = stmt.executeUpdate();
		stmt.close();
		return res;
	}

	private boolean isEmptyFolder(File f) {

		if (f.exists() && f.listFiles() != null && f.listFiles().length == 0) {
			return true;
		}

		return false;
	}

	/**
	 * Recursively deletes a folder with all it's subfolders and files.
	 *
	 * @param folder
	 *            the folder to delete.
	 */
	public void recursiveDelete(File folder) {

		File[] contents = folder.listFiles();

		if (contents == null || contents.length == 0) {
			folder.delete();
		} else {
			for (File f : contents) {
				recursiveDelete(f);
			}
		}

		folder.delete();
	}

	/**
	 * Checks if the file with the specified fileID exists in the database.
	 *
	 * @param fileID
	 *            int - the fileID of the file.
	 * @return true if the file exists, else false.
	 * @throws SQLException
	 */
	public boolean hasFile(int fileID) throws SQLException {

		String query = "SELECT fileID FROM File " + "WHERE fileID = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, fileID);

		ResultSet rs = stmt.executeQuery();
		boolean hasResult = rs.next();

		if (rs.next()) {
			hasResult = false;
		}

		stmt.close();

		return hasResult;
	}

	/**
	 * Sets the status of a file to "Done".
	 *
	 * @param fileID
	 *            the ID of the file to set to "Done".
	 * @return the number of tuples updated.
	 * @throws SQLException
	 */

	public int fileReadyForDownload(int fileID) throws SQLException {

		String statusUpdateString = "UPDATE File SET Status = 'Done' "
				+ "WHERE FileID = ?";

		PreparedStatement statusUpdate = conn
				.prepareStatement(statusUpdateString);
		statusUpdate.setInt(1, fileID);

		int resCount = statusUpdate.executeUpdate();
		statusUpdate.close();

		return resCount;
	}

	/**
	 * Changes the Filename for a specific file with given fileID. This method
	 * affects bothe the saved file name, but also the entries path and fileName
	 * in database.
	 *
	 * @return resCount int, the number of rows affected by the change.
	 * @throws SQLException
	 *             if failed to send query,
	 * @throws IOException
	 *             if the chosen new file name already exist as a stored file.
	 */
	public int changeFileName(int fileID, String newFileName)
			throws SQLException, IOException {

		String oldFilePath = "";

		// search for current filepath.
		String searchPathQuery = "SELECT Path FROM File WHERE fileID = ?";
		PreparedStatement pathFind = conn.prepareStatement(searchPathQuery);
		pathFind.setInt(1, fileID);
		ResultSet res = pathFind.executeQuery();

		if (res.next()) {
			oldFilePath = res.getString("Path");
		} else {
			throw new IOException("No file with ID " + fileID);
		}

		String folderPath = getParentFolder(oldFilePath);
		String newFilePath = folderPath + newFileName;

		// change name on the actual stored file.
		File oldfile = new File(oldFilePath);
		File newFile = new File(newFilePath);

		if (newFile.exists())
			throw new java.io.IOException("New file exists");

		if (!oldfile.exists())
			throw new java.io.IOException("Old file " + "does not exists");

		String chFileNameQuery = "UPDATE File SET FileName = ? "
				+ "WHERE FileID = ?";

		PreparedStatement nameUpdate = conn.prepareStatement(chFileNameQuery);
		nameUpdate.setString(1, newFileName);
		nameUpdate.setInt(2, fileID);
		int resCount = nameUpdate.executeUpdate();

		oldfile.renameTo(newFile);

		// change filepath entry in database.
		String chFilePathQuery = "UPDATE File SET Path = ? "
				+ "WHERE FileID = ?";

		PreparedStatement pathUpdate = conn.prepareStatement(chFilePathQuery);
		pathUpdate.setString(1, newFilePath);
		pathUpdate.setInt(2, fileID);
		pathUpdate.executeUpdate();

		nameUpdate.close();
		pathUpdate.close();

		return resCount;
	}

	public FileTuple addGeneratedFile(String expId, int fileType,
			String filePath, String inputFileName, String metaData,
			String uploader, boolean isPrivate, String grVersion)
			throws SQLException, IOException {

		Experiment e = expMethods.getExperiment(expId);

		if (e == null) {
			throw new IOException("The experiment " + expId
					+ " does not exist!");
		}

		expId = e.getID();
		String inputFilePath = null;

		if (inputFileName != null) {
			inputFilePath = getParentFolder(filePath) + inputFileName;
		}

		String query = "INSERT INTO File "
				+ "(Path, FileType, FileName, Date, MetaData, InputFilePath, "
				+ "Author, Uploader, IsPrivate, ExpID, GRVersion, Status) "
				+ "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, 'Genomizer',"
				+ " ?, ?, ?, ?, 'Done')";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, filePath);

		switch (fileType) {
		case FileTuple.RAW:
			stmt.setString(2, "Raw");
			break;
		case FileTuple.PROFILE:
			stmt.setString(2, "Profile");
			break;
		case FileTuple.REGION:
			stmt.setString(2, "Region");
			break;
		default:
			stmt.setString(2, "Other");
			break;
		}

		stmt.setString(3, getFileName(filePath));
		stmt.setString(4, metaData);
		stmt.setString(5, inputFilePath);
		stmt.setString(6, uploader);
		stmt.setBoolean(7, isPrivate);
		stmt.setString(8, expId);
		stmt.setString(9, grVersion);

		stmt.executeUpdate();
		stmt.close();

		return getFileTuple(filePath);
	}
}
