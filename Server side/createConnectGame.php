<?php
require_once 'logindb.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

mysql_select_db($db_database, $db_server)
or die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

if (isset($_POST['userId']))
{
	$userId = get_post('userId');
	$pending = true;
	
	if(!isset($_POST['ConnectToGame']))
	{
		$query = "INSERT INTO games (`pending`, `creator_userId`) VALUES ($pending , '$userId')";
		$result = mysql_query($query, $db_server);
		if (!$result)
		{
			echo "<?xml version='1.0' encoding='UTF-8'?><error>INSERT failed</error>";
			exit;
		}
		
		$gameId = mysql_insert_id();
	}
	else
	{
		$gameId = get_post('gameId');
	}
	
	$sql="SELECT users_in_game.gameId, users_in_game.userId, games.pending, games.creator_userId
			FROM users_in_game
			LEFT JOIN games
			ON users_in_game.gameId=games.id WHERE users_in_game.userId = '$userId' && users_in_game.gameId = '$gameId'";
	$result=mysql_query($sql);
	
	$count=mysql_num_rows($result);
	if($count==0)
	{
		$query = "INSERT INTO users_in_game (`gameId`, `userId`) VALUES('$gameId', '$userId')";
		if (!mysql_query($query, $db_server))
		{
			echo "<?xml version='1.0' encoding='UTF-8'?><error>INSERT failed</error>";
			exit;
		}
	}
	
	$sql="SELECT users_in_game.gameId, users_in_game.userId, games.pending, games.creator_userId
			FROM users_in_game
			LEFT JOIN games
			ON users_in_game.gameId=games.id WHERE users_in_game.userId = '$userId' && users_in_game.gameId = '$gameId'";
	$result=mysql_query($sql);
	while($row = mysql_fetch_array($result))
	{
		$gameId = $row['gameId'];
		$pending = $row['pending'];
		$creator_userId = $row['creator_userId'];
		
	}
	
	echo "<?xml version='1.0' encoding='UTF-8'?><gameId>$gameId</gameId><pending>$pending</pending><creator_userId>$creator_userId</creator_userId>";
	exit;
	
}

mysql_close($db_server);

function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}
?>