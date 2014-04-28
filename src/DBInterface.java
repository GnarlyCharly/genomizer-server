
import java.util.ArrayList;
import java.util.HashMap;

public class DBInterface {

	/*Metoder som m�ste g�ras:
	 * metod f�r att l�gga ihop annotationer i s�kning (te x where + blala AND + sdfsdfkdfl...)
	 * metod som genererar s�kv�gar inkl filnamn (OCH SKAPAR MAPPAR)
	 *r
	 * */


	//USERS

	/**
	 * Gets user password
	 *
	 * @param String userName
	 * @return String password
	 */
	public String getPassword(String userName) {
		//SELECT Password FROM User WHERE Username = userName
		return null;
	}

	/**
	 * Sets user password
	 *
	 * @param String userName
	 * @param String newPassword
	 */
	public void setPassword(String userName, String newPassword) {
		//UPDATE USER SET Password = newPassword WHERE Username = userName
	}

	/**
	 * Gets user role
	 *
	 * @param String userName
	 * @return String userRole
	 */
	public String getUserPermissions(String userName) {
		//SELECT Role FROM User WHERE Username = userName
		return null;
	}

	/**
	 * Sets user role
	 *
	 * @param String userName
	 * @param String newRole
	 */
	public void setUserPermissions(String userName, String newRole) {
		//UPDATE USER SET Role = newRole WHERE Username = userName
	}

	/**
	 * Gets list of all users
	 *
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getUsers() {
		//SELECT * FROM User
		return null;
	}

	/**
	 * Adds a new user
	 * Taken RW and KK!!!
	 * @param String userName
	 * @param String password
	 * @param String role
	 */
	public void addUser(String userName, String password, String role) {
		//INSERT INTO UserInfo(Username, Password, Role) VALUES(userName, password, role)
	}

	/**
	 * Deletes a user and his/her workspaces
	 * Returns list of removed workspace(s) files
	 *
	 * @param String userName
	 * @return ArrayList<AnnoObj>
	 */
	public ArrayList<AnnoObj> removeUser(String userName) {
		//DELETE FROM Working_On WHERE (Username = userName)
		//DELETE FROM UserInfo WHERE (Username = userName)
		return null;
	}


	//Files

	/**
	 * Gets the annotations of a file
	 *
	 * @param String fileID
	 * @return AnnoObj
	 */
	public AnnoObj getAnnotations(String fileID) {
		//SELECT * FROM Experiment WHERE (ExpID =
		//(SELECT ExperimentID FROM File WHERE(FileID = "fileID"))
		return null;
	}

	/**
	 * Creates a new annotation column in database table
	 *
	 * @param String colName
	 * @param String value
	 */
	public void addAnnotation(String colName, String value) {

	}

	/**
	 * Makes a database search and returns a list of experiments
	 * with annotations
	 *
	 * @param AnnoObj annotations
	 * @return ArrayList<AnnoOb
	 */
	public ArrayList<AnnoObj> searchExperiment(AnnoObj annotations) {
		//SELECT ExpId FROM Experiment WHERE ("Insert Str here)
		return null;
	}

	/**
	 *
	 * @param annotations
	 * @return
	 */
	public ArrayList<AnnoObj> searchFile(AnnoObj annotations) {
		//SELECT * FROM File WHERE ("Insert Str here)
		return null;
	}

	/**
	 * Creates and returns a filepath for uploading of a file
	 * Returns null if path is already taken.
	 *
	 * @param AnnoObj annotations
	 * @return String filepathAnnoObj
	 */
	public String uploadFile(AnnoObj annotations) {
		//
		return null;
	}

	/**
	 * Gets a filepath of a file so it can be accessed for download
	 *
	 * @param AnnoObj annotations
	 * @return String filepath
	 */
	public String downloadFile(AnnoObj annotations) {
		return null;
	}

	/**
	 * Deletes file from database, returns the path
	 * for physical deletion in file system
	 *
	 * @param String FileID
	 * @return String filepath
	 */
	public String deleteFile(String fileID) {
		//DELETE FROM Published_In WHERE (FileID = fileID)
		//DELETE FROM Used_In WHERE (FileID = fileID)
		//DELETE FROM File WHERE (FileID = fileID)

		return null;
	}


	//WORKSPACE

	/**
	 * Creates a new workspace entry
	 *
	 * @param String workSpaceID
	 * @param String userName
	 */
	public void createWorkSpace(String workSpaceID, String userName) {

	}

	/**
	 * Removes a workspace entry
	 *
	 * @param String workSpaceID
	 * @param String userName
	 */
	public void removeWorkSpace(String workSpaceID, String userName) {

	}

	/**
	 * Adds a file to a workspace(not adding to actual database)
	 *
	 * @param String fileID
	 * @param String userName
	 * @param String workSpaceID
	 */
	public void addToWorkSpace(String fileID, String userName,
			String workSpaceID) {

	}

	/**
	 * Removes a file from workspace(not removing from actual database)
	 *
	 * @param String fileID
	 * @param String userName
	 * @param String workSpaceID
	 */
	public void removeFromWorkSpace(String fileID, String userName,
			String workSpaceID) {

	}

	/**
	 * Shares a workspace with another user
	 *
	 * @param String workSpaceID
	 * @param String ownerName, user who owns the workspace
	 * @param String userName, user to share workspace with
	 */
	public void shareWorkSpace(String workSpaceID, String ownerName,
			String userName) {
	}

	/**
	 * Returns a hashmap containing all workspaces. The workspace name is the
	 * value and ID the key.
	 *
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getAllWorkSpaces() {

		return null;
	}

}