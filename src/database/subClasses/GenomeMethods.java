package database.subClasses;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.FilePathGenerator;
import database.ServerDependentValues;

/**
 * Class that contains all the methods for adding,changing, getting and
 * removing genome releases and chain files. This class is a subClass of
 * databaseAcessor.java.
 *
 * date: 2014-05-14
 * version: 1.0
 */
public class GenomeMethods {

	private Connection conn;
	private FilePathGenerator fpg;

	/**
	 * constructor for the genomeMethods class.
	 * @param connection Connection, the database jdbc connection.
	 * @param filePG FilePathGenerator, object reference to that class.
	 */
	public GenomeMethods(Connection connection, FilePathGenerator filePG){

		conn = connection;
		fpg = filePG;
	}

	/**
     * Gets the file path to a stored Genome Release
     * @param genomeVersion - The version to get filepath to,
     * should use getAllGenomeReleases()
     * and let user choose a version
     * @return String path - a file path
     * @throws SQLException
     */

    public String getGenomeRelease(String genomeVersion) throws SQLException{

        String query ="SELECT FilePath FROM Genome_Release WHERE (Version = ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, genomeVersion);
        ResultSet rs = stmt.executeQuery();
        String path = null;

        if (rs.next()) {
            path = rs.getString("FilePath");
        }

        stmt.close();

    	return path;
    }

    /**
     * Add one genomerelease to the database.
     *
     * @param String
     *            genomeVersion.
     * @param String
     *            species.
     * @return String The path to the folder where the genome release
     *         files should be saved.
     * @throws SQLException
     *             if adding query failed.
     */
    public String addGenomeRelease(String genomeVersion,
            String species, String filename) throws SQLException {

        String folderPath = fpg.generateGenomeReleaseFolder(genomeVersion,
                species);

        StringBuilder filePathBuilder = new StringBuilder(folderPath);
        filePathBuilder.append(filename);

        String filePath = filePathBuilder.toString();

        String query = "INSERT INTO Genome_Release "
                + "(Version, Species, FilePath) " + "VALUES (?, ?, ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, genomeVersion);
        stmt.setString(2, species);
        stmt.setString(3, filePath.toString());

        stmt.execute();

        filePathBuilder.insert(0, ServerDependentValues.UploadURL);
        stmt.close();
        return filePathBuilder.toString();
    }

    /**
     * Removes one specific genome version stored in the database.
     *
     * @param version
     *            , the genome version.
     * @param specie
     *            .
     * @return boolean, true if succeded, false if failed.
     */
    public boolean removeGenomeRelease(String genomeVersion,
            String specie) {

        String query = "DELETE FROM Genome_Release " +
        		"WHERE (Version = ? AND Species = ?)";

        PreparedStatement stmt;

        try {
            stmt = conn.prepareStatement(query);

            stmt.setString(1, genomeVersion);
            stmt.setString(2, specie);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Failed to remove genome release!");
            return false;
        }
        return true;
    }

    /**
    * method for getting all the genome releases currently stored in the
    * database.
    * @param species String, the name of the specie you want to get genome
    * 		releases for.
    * @return genomeVersions List<String>, list of all the genome releases for
    * 		a specific species.
    * @throws SQLException
    */
   public List<String> getAllGenomReleases(String species) throws SQLException {

        List<String> versionsList = new ArrayList<String>();
        String query = "SELECT Version FROM Genome_Release WHERE Species = ?";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, species);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            versionsList.add(rs.getString("Version"));
        }

        stmt.close();
        return versionsList;
    }

   /**
    * get a specific chainfile depending on from and to what genome release you
    * want to convert between.
    * @param fromVersion String, the name of the old genome release version
    * @param toVersion String, the name of the new genome release version
    * @return resFilePath String, the filePath of that chain file.
    * @throws SQLException
    */
    public String getChainFile(String fromVersion, String toVersion)
            throws SQLException {

        String query = "SELECT FilePath FROM Chain_File WHERE (FromVersion = ?)"
                + " AND (ToVersion = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, fromVersion);
        stmt.setString(2, toVersion);
        ResultSet rs = stmt.executeQuery();
        String resFilePath = null;

        if (rs.next()) {
        	resFilePath = rs.getString("FilePath");
        }

        stmt.close();

        return resFilePath;
    }

    /**
     * Adds a chain file to database for conversions. Parameters:
     * Oldversion, new version and filename. Returns: upload URL
     *
     * @param String
     *            fromVersion
     * @param String
     *            toVersion
     * @param String
     *            fileName
     * @return String upload URL
     * @throws SQLException
     */
    public String addChainFile(String fromVersion, String toVersion,
            String fileName) throws SQLException {

        String species = "";
        String speciesQuery = "SELECT Species From Genome_Release"
                + " WHERE (version = ?)";

        PreparedStatement speciesStat = conn.prepareStatement(speciesQuery);
        speciesStat.setString(1, fromVersion);

        ResultSet rs = speciesStat.executeQuery();

        while (rs.next()) {
            species = rs.getString("Species");
        }
        speciesStat.close();

        String filePath = fpg.generateChainFolderPath(species, fromVersion,
                toVersion) + fileName;

        String insertQuery = "INSERT INTO Chain_File "
                + "(FromVersion, ToVersion, FilePath) VALUES (?, ?, ?)";

        PreparedStatement insertStat = conn.prepareStatement(insertQuery);
        insertStat.setString(1, fromVersion);
        insertStat.setString(2, toVersion);
        insertStat.setString(3, filePath);
        insertStat.executeUpdate();
        insertStat.close();

        String URL = ServerDependentValues.UploadURL;

        return URL + filePath;
    }

    /**
     * Deletes a chain_file from the database. You find the unique
     * file by sending in the genome version the file converts from
     * and the genome version the file converts to.
     *
     * @param fromVersion
     *            - genome version the Chain_file converts from
     * @param toVersion
     *            - genome version the Chin_file converts to
     * @return the number of deleted tuples in the database. (Should
     *         be one if success)
     * @throws SQLException
     *             - if the query does not succeed
     */
    public int removeChainFile(String fromVersion, String toVersion)
            throws SQLException {

        String query = "DELETE FROM Chain_File WHERE (FromVersion = ?)"
                + " AND (ToVersion = ?)";

        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, fromVersion);
        stmt.setString(2, toVersion);

        int resCount = stmt.executeUpdate();
        stmt.close();

        return resCount;
    }
}