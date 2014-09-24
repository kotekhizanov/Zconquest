<?php
require_once 'logindb.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

mysql_select_db($db_database, $db_server)
or die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

if (isset($_POST['userId']))
{
	$userId = get_post('userId');
	
	if(!isset($_POST['ConnectToGame']))
	{
		CreateGame($userId);
	}
	else
	{
		$gameId = get_post('gameId');
	}
	
	AddUserToGame($gameId, $userId);
	
	GenrateGameStatusXML($gameId);
	
	exit;	
}




mysql_close($db_server);

function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}
?>