<?php
#A php-script for uploading to the server.
#Auths the user against the DB.


#TODO: Fix return codes

#Check if user has inputed username
if (($_SERVER['PHP_AUTH_USER']) == "") {
	header('WWW-Authenticate: Basic realm="My Realm"');
	header('HTTP/1.0 401 Unauthorized');
	echo 'You need to set a user and password.';
	http_response_code(401);
	exit;
} else {
#	echo "<p>Hello {$_SERVER['PHP_AUTH_USER']}.</p>\n";
#	echo "<p>You entered {$_SERVER['PHP_AUTH_PW']} as your password.</p>\n";
	#Check user authentication
	$access = authenticateUser($_SERVER['PHP_AUTH_USER'], $_SERVER['PHP_AUTH_PW']);
}

#Start uploading if user exist in the database with the correct password
if($access != "none") {
	echo "authed uploading file..\n";
	uploadFile();
} else {
	echo "no access!\n";
	http_response_code(401);
}

#Upload a file
function uploadFile() {

	$file_name = basename($_POST['path']);
	$destination_path = dirname($_POST['path']) . "/";
	$target_path = $destination_path . $file_name;
	$hasExtension = checkHasExtension($target_path);
#	$hasExtension = True;

#	echo "Source=" .        basename($destination_path) . "<br />";
#	echo "Destination=" .   $destination_path . "<br />";
#	echo "Target path=" .   $target_path . "<br />";
#	echo "Size=" .          $_FILES['uploadfile']['size'] . "<br />";

	#Create directory if it doesn't exist
	if(!is_dir($destination_path)) {
		mkdir($destination_path);
	}

	#If the file doesn't have an extension, send error.
	if(!$hasExtension){
		echo "Error: File has no extension.<br />\n";
		http_response_code(406);
	#Upload(move) the file to the correct folder
	} else if(move_uploaded_file($_FILES['uploadfile']['tmp_name'], $target_path)) {
		echo "The file ".  $file_name.
			" has been uploaded\n";
		http_response_code(201);
	}
}

#Checks if the file has a file-type extension
function checkHasExtension($file_name){
	$file_parts = pathinfo($file_name);

	if($file_parts['extension'] == ""){
		echo "no extension<br />\n";
		return FALSE;
	} else {
		echo "has extension<br />\n";
		return TRUE;
	}
}

#Increments the filename with a number (Deprecated)
function incrementFileName($file_path,$filename){
	if(count(glob($file_path.$filename))>0)
	{
		$file_ext = end(explode(".", $filename));
		$file_name = str_replace(('.'.$file_ext),"",$filename);
		$newfilename = $file_name.'_'.count(glob($file_path."$file_name*.$file_ext")).'.'.$file_ext;
		echo "changed filename<br />\n";
		return $newfilename;
	}
	else
  	{
		echo "did not change filename<br />\n";
		return $filename;
	}
}

#Tries to authenticate the user and return the user rights.
function authenticateUser($user_name, $pswd) {

#TODO: Hide the the DB login info somewhere else
$host = "localhost";
$port = "60000";
$user = "postgres";
$pass = "pvt";
$db = "Test";

#Connect to the database
$con = pg_connect("host=$host port=$port dbname=$db user=$user
         password=$pass")
         or die ("Could not connect to dabasase\n");

#Query to get all the users from the DB
$query = "SELECT * FROM user_info";

#Result of the query.
$result = pg_query($con, $query) or die("Cannot execute query: $query\n");

#Initial access rights.
$access = "none";

#Check the suplied user and password against the DB users, set the access rights if the user was found.
while ($row = pg_fetch_row($result)) {
	if($user_name == $row[0] && $pswd == $row[1]) {
		$access = $row[2];
	}

}

#Close DB connection.
pg_close($con);

#Return the user rights.
return $access;
}

?>
