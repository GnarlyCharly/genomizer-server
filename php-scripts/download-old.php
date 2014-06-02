<?php

#A script to download a file from the server.
#Auths the user against the DB.

#Path to the file to download
$URI = $_GET['path'];

#Checks if the user has inputed username.
if (($_SERVER['PHP_AUTH_USER']) == "") {
        header('WWW-Authenticate: Basic realm="My Realm"');
        header('HTTP/1.0 401 Unauthorized');
        echo 'You need to set a user and password.';
        http_response_code(401);
        exit;
} else {
#        echo "<p>Hello {$_SERVER['PHP_AUTH_USER']}.</p>\n";
#        echo "<p>You entered {$_SERVER['PHP_AUTH_PW']} as your password.</p>\n";
	#Checks the users user-rights.
        $access = authenticateUser($_SERVER['PHP_AUTH_USER'], $_SERVER['PHP_AUTH_PW']);
}

if($access != "none") {
	echo "downloading file..\n";

	#If the file exists send the file.
	if (file_exists($URI)) {
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
		exit;
	} else {
		echo "File not found.";
		exit;
	}
} else {
        echo "no access!\n";
        http_response_code(401);
}


#Authenticates a user and returns the user-rights.
function authenticateUser($user_name, $pswd) {

	#TODO: Hide the DB auth info somewhere.
	$host = "localhost";
	$port = "60000";
	$user = "postgres";
	$pass = "pvt";
	$db = "Test";

	#Connects to the DB.
	$con = pg_connect("host=$host port=$port dbname=$db user=$user
	         password=$pass")
	         or die ("Could not connect to dabasase\n");

	#Query that gets all users from the DB.
	$query = "SELECT * FROM user_info";

	#Result of the query.
	$result = pg_query($con, $query) or die("Cannot execute query: $query\n");

	#Initial user-rights.
	$access = "none";

	#Checks every user in the DB against the suplied username and password.
	#Then sets the user-rights.
	while ($row = pg_fetch_row($result)) {
	        if($user_name == $row[0] && $pswd == $row[1]) {
	                $access = $row[2];
	        }

	}

	#Close the DB connection.
	pg_close($con);

	#Returns the user-rights.
	return $access;
}


?>