<?php
require_once 'login.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("Error: Unable to connect");

mysql_select_db($db_database, $db_server)
or die("Error: Unable to connect");

$query = "INSERT INTO games (`pending`) VALUES(true)";
if (isset($_POST['userId']))
{
	if (mysql_query($query, $db_server))
	{
		$gameId = mysql_insert_id();
		$userId = get_post('userId');
		
		$query = "INSERT INTO users_in_game (`gameId`, `userId`) VALUES('$gameId', '$userId')";
		if (!mysql_query($query, $db_server))
		{
			echo "Error: INSERT failed";
		}
	}
	else
	{
		echo "Error: INSERT failed";
	}
}

mysql_close($db_server);

function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}
?>