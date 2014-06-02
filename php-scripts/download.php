<?php

#A script to download a file from the server.
#Auths the user against the DB.

#TODO: Add a trusted site instead of *.
header('Access-Control-Allow-Origin: *', false);
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE', false);
header('Access-Control-Allow-Headers: Authorization', false);

#Path to the file to download
$URI = $_GET['path'];

echo $URI;
$headers = apache_request_headers();

#Gets the token, either as a header or a parameter.
if($headers['Authorization'] == ""){
	$access = auth_user($_GET['token']);
} else {
	$access = auth_user($headers['Authorization']);
}

$path = dirname($URI) . "/";

if($access == 200) {
	#If the file exists send the file.
	if (validate_path_db($URI, $path) != "none" AND file_exists($URI)) {
		header('Content-Description: File Transfer');
		header('Content-Type: application/octet-stream');
		header('Content-Disposition: attachment; filename='.basename($URI));
		header('Expires: 0');
		header('Cache-Control: must-revalidate');
		header('Pragma: public');
		header('Content-Length: ' . filesize($URI));
		ob_clean();
		flush();
		readfile($URI);
		http_response_code(200);
		log_activity($URI, $access);
	} else {
		http_response_code(404);
		echo "ERROR: File not found.";
	}
} else {
        echo "ERROR: Access denied!\n";
        http_response_code(401);
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

        return $status;
}

#Checks if the given path is in /var/www/data/ and that
#it doesn't contain '..' (DEPRECATED).
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

#Checks if a given path is present in the database.
function validate_path_db($file_path, $path){

	#Connect to the database
	$dbh = connect_to_db();
	#Check file path
	$stmt = $dbh->prepare("SELECT path FROM file WHERE path=? AND status='Done'");
	$stmt->bindParam(1, $param);

	$param = $file_path;
	$stmt->execute();
	$row = $stmt->fetch();

	#Check genome release path
	$stmt = $dbh->prepare("SELECT folderpath FROM genome_release NATURAL JOIN genome_release_files 
				WHERE folderpath=? AND status='Done'");
	$stmt->bindParam(1, $param);

	$param = $path;
	$stmt->execute();
	$row2 = $stmt->fetch();

	#Check chain file path
	$stmt = $dbh->prepare("SELECT folderpath FROM chain_file NATURAL JOIN chain_file_files 
				WHERE folderpath=? AND status='Done'");
	$stmt->bindParam(1, $param);

	$param = $path;
	$stmt->execute();
	$row3 = $stmt->fetch();

	#Close connection
	$dbh = null;

	log_var("db validate: " . $row[0] . " OR " . $row2[0] . " OR " . $row3[0] . "\n");

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

#Help function for logging stuff in the script
function log_var($variable) {

        $log_file = 'log2_variable.txt';

        $current_log = file_get_contents($log_file);


        $current_log .= "START LOG: " . date('l jS \of F Y h:i:s A') . "\n";
        $current_log .= "variable: " . $variable . "\n";

        $current_log .= "END OF LOG\n";
        file_put_contents($log_file, $current_log);

}


function log_activity($filename, $token) {
        
        $log_file = 'phplogdownload.txt';

        $current_log = file_get_contents($log_file);


        $current_log .= "START LOG: " . date('l jS \of F Y h:i:s A') . "\n";
        $current_log .= "IP: " . $_SERVER['REMOTE_ADDR'] . "\n";
        $current_log .= "FORWARD IP: " . $_SERVER['HTTP_X_FORWARDED_FOR'] . "\n";
        $current_log .= "username: " . $_SERVER['PHP_AUTH_USER'] . "\n";
        $current_log .= "auth response: " . $token . "\n";
        $current_log .= "auth token: " . $_SERVER['Authorization'] . "\n";

        $current_log .= "filename: " . $filename . "\n";
        $current_log .= "END OF LOG\n";
        file_put_contents($log_file, $current_log);

}


?>
