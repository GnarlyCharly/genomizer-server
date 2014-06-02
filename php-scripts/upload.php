<?php
#A php-script for uploading to the server.
#Auths the user against the DB.


#Add a trusted site instead of *.
header('Access-Control-Allow-Origin: *', false);
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE', false);
header('Access-Control-Allow-Headers: Authorization', false);

$headers = apache_request_headers();
$access = auth_user($headers['Authorization']);

#Start uploading if user exist in the database with the correct password
if($access == 200) {
#	echo "authed uploading file..\n";
	upload_file($access);
} else {
	echo "ERROR: Access denied!\n";
	http_response_code(401);
}

#Upload a file
function upload_file($access) {

	$file_name = basename($_GET['path']);

	if($file_name == "") {
		$file_name = basename($_POST['path']);
	}

	$path = $_GET['path'];
	if($path == ""){
		$path = $_POST['path'];
	}
	
	$destination_path = dirname($path) . "/";
	$target_path = $destination_path . $file_name;

#	echo "Source=" .        basename($destination_path) . "\n";
#	echo "Destination=" .   $destination_path . "\n";
#	echo "Target path=" .   $target_path . "\n";
#	echo "Size=" .          $_FILES['uploadfile']['size'] . "\n";

	$success = FALSE;

	log_var("upload path uri: " . $target_path . " path: " . $destination_path . "\n");


	$filetype = validate_path_db($target_path, $destination_path);

	#Validates the given filepath.
	if($filetype == "none") {
		echo "ERROR: Invalid filepath\n";
		http_response_code(403);
	#Upload(move) the file to the correct folder
	} else if(move_uploaded_file($_FILES['uploadfile']['tmp_name'], $target_path)) {
		$success = TRUE;
		echo "The file " .  $file_name .
			" has been uploaded successfully\n";
		#log upload
		log_activity($_GET['path']);
		if($filetype == "file"){
			change_to_done($target_path, $file_name, $filetype);
		} else {
			change_to_done($destination_path, $file_name, $filetype);
		}
		http_response_code(201);
	} else {
		echo "ERROR: Failed to move temp-file to upload location\n";
		http_response_code(403);
	}

#	if(!$success) {
		#send_delete($_GET['path'], $access);
#		echo "Failed to upload file\n";	
#		http_response_code(403);
#	}
}

#Checks if the file has a file-type extension (DEPRECATED).
function check_has_extension($file_name){
	$file_parts = pathinfo($file_name);

	if($file_parts['extension'] == ""){
		echo "filename: " . $file_name . "\n";
		echo "no extension\n";
		return FALSE;
	} else {
		echo "has extension\n";
		return TRUE;
	}
}

#Get info for connection to the DB from a file
#'settings.cfg' that haveto be placed in the same folder as the script.
function connect_to_db(){
        $file = file_get_contents('settings.cfg', true);
        $dbinfo = explode("\n", $file);

        for($i = 0; $i < 4; $i++){
                switch(strtolower(trim(explode("=", $dbinfo[$i])[0]))) {
                        case "databaseuser":
                                $user = trim(explode("=", $dbinfo[$i])[1]);
                                break;
                        case "databasepassword":
                                $pass = trim(explode("=", $dbinfo[$i])[1]);
                                break;
                        case "databasehost":
                                $host = explode(":", trim(explode("=", $dbinfo[$i])[1]))[0];
                                $port = explode(":", trim(explode("=", $dbinfo[$i])[1]))[1];
                                break;
                        case "databasename":
                                $db = trim(explode("=", $dbinfo[$i])[1]);
                                break;
                        default:
                                break;
                }
        }

        $dbh = new PDO("pgsql:dbname=$db;host=$host;port=$port", $user, $pass);
        return $dbh;


}


#Sets the status of file to 'Done'
function change_to_done($file_path, $filename, $type){

	#Connect to the database
	$dbh = connect_to_db();
	#Check file path
	if($type == "file"){
		$stmt = $dbh->prepare("UPDATE file SET status='Done' WHERE path=? AND status<>'Done'");
		$stmt->bindParam(1, $param);

		$param = $file_path;
	#Check genome release path
	} else if($type == "genome"){
		$stmt = $dbh->prepare("UPDATE genome_release_files SET status='Done'
					FROM genome_release_files AS G1
					NATURAL JOIN genome_release AS G2
					WHERE genome_release_files.version=G1.version AND
						G2.folderpath=? AND 
						genome_release_files.filename=? AND
						G1.status<>'Done'");
		$stmt->bindParam(1, $param);
		$stmt->bindParam(2, $param2);		

		$param = $file_path;
		$param2 = $filename;
	#Check chain file path
	} else if($type == "chain"){
		$stmt = $dbh->prepare("UPDATE chain_file_files SET status='Done'
					FROM chain_file_files AS G1
					NATRUAL JOIN chain_file AS G2
					WHERE 	chain_file_files.fromversion=G1.fromversion AND
						chain_file_files.toversion=G1.toversion AND
						G2.folderpath=? AND 
						genome_release_files.filename=? AND
						G1.status<>'Done'");
		$stmt->bindParam(1, $param);
		$stmt->bindParam(2, $param2);		

		$param = $file_path;
		$param2 = $filename;

	}

	

	$stmt->execute();
	$row = $stmt->fetch();
	
#	log_var("sql response: " . $row[0]);
}

#Sends a delete request to the server to delete a file from the DB
function send_delete($file_path, $token) {

	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, "localhost:7000/file/" . $file_path);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
	curl_setopt($ch, CURLOPT_HEADER, FALSE);
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
	curl_setopt($ch, CURLOPT_HTTPHEADER, array("Authorization: " . $token));
	$response = curl_exec($ch);
	$status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
	curl_close($ch);
	log_var($response);
	log_var($status);

}

#Increments the filename with a number (Deprecated)
function increment_fileName($file_path,$filename){
	if(count(glob($file_path.$filename))>0){
		$file_ext = end(explode(".", $filename));
		$temp_filename = str_replace(('.'.$file_ext),"",$filename);
		$new_file_name = $filename.'_'.count(glob($file_path."$temp_filename*.$file_ext")).'.'.$file_ext;
		echo "changed filename\n";
		return $new_filename;
	}else{
		echo "did not change filename\n";
		return $filename;
	}
}

#Checks if a given token is valid against the server
function auth_user($token){
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, "localhost:7000/token");
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
	curl_setopt($ch, CURLOPT_HEADER, FALSE);
	curl_setopt($ch, CURLOPT_HTTPHEADER, array("Authorization: " . $token));
	$response = curl_exec($ch);
	$status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
	curl_close($ch);

	log_var("auth: " . $status);

	return $status;
}

#Checks if a file path is valid. (DEPRECATED).
function validate_file_path($file_path){
	if(substr($file_path, 0, 14) == "/var/www/data/") {
		if(strpos($file_path, "..") !== FALSE) {
			return FALSE;
		} else {
			return TRUE;
		}
	} else {
		return FALSE;
	}
}

#Checks if a given filepath is present the DB.
function validate_path_db($file_path, $path){

	#Connect to the database
	$dbh = connect_to_db();

	#Check file path
	$stmt = $dbh->prepare("SELECT path FROM file WHERE path=? AND status<>'Done'");
	$stmt->bindParam(1, $param);

	$param = $file_path;
	$stmt->execute();
	$row = $stmt->fetch();

	#Check genome release path
	$stmt = $dbh->prepare("SELECT folderpath FROM genome_release NATURAL JOIN genome_release_files
				 WHERE folderpath=? AND status<>'Done'");
	$stmt->bindParam(1, $param);

	$param = $path;
	$stmt->execute();
	$row2 = $stmt->fetch();

	#Check chain file path
	$stmt = $dbh->prepare("SELECT folderpath FROM chain_file NATURAL JOIN chain_file_files
				 WHERE folderpath=? AND status<>'Done'");
	$stmt->bindParam(1, $param);

	$param = $path;
	$stmt->execute();
	$row3 = $stmt->fetch();

	#Close connection
	$dbh = null;

	log_var("filetype: " . $row[0] . " or " . $row2[0] . " or " . $row3[0] . "\n");

	#Return true if the file path was found in the DB.
	if($row[0] == $file_path){
		return "file";
	}else if($row2[0] == $path){
		return "genome";
	}else if($row3[0] == $path){
		return "chain";
	} else {
		return "none";
	}
}

function log_activity($filename) {

	$log_file = 'phplogupload.txt';

	$current_log = file_get_contents($log_file);


	$current_log .= "START LOG: " . date('l jS \of F Y h:i:s A') . "\n";
	$current_log .= "IP: " . $_SERVER['REMOTE_ADDR'] . "\n";
	$current_log .= "FORWARD IP: " . $_SERVER['HTTP_X_FORWARDED_FOR'] . "\n";
	$current_log .= "username: " . $_SERVER['PHP_AUTH_USER'] . "\n";

	$current_log .= "filename: " . $filename . "\n";
	$current_log .= "END OF LOG\n";
	file_put_contents($log_file, $current_log);

}

#Help function for logging stuff in the script
function log_var($variable) {

	$log_file = 'variable_log.txt';

	$current_log = file_get_contents($log_file);


	$current_log .= "START LOG: " . date('l jS \of F Y h:i:s A') . "\n";
	$current_log .= "variable: " . $variable . "\n";

	$current_log .= "END OF LOG\n";
	file_put_contents($log_file, $current_log);

}


?>
