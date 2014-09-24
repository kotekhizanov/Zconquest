<?php
require_once 'logindb.php';
require_once 'GameLogics.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

mysql_select_db($db_database, $db_server)
or die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

if (isset($_POST['gameId']) && isset($_POST['planetAmount']))
{
	$gameId = get_post('gameId');
	$planetAmount = get_post('planetAmount');
	
	$PlanetList = Array();
	
	GeneratePlanets($db_server, $PlanetList, $gameId, $planetAmount);

	SetUsersToRandomPlanets($db_server, $PlanetList, $gameId);
	
	GenrateGameStatusXML($gameId);
}



mysql_close($db_server);

function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}
?>