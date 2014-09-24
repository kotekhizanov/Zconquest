<?php
require_once 'logindb.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

mysql_select_db($db_database, $db_server)
or die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

if (isset($_POST['functionName']))
{
	$functionName = $_POST['functionName'];
}
else
die();

if(isset($_POST['gameId']))
{
	$gameId = get_post('gameId');
}

if ($functionName == "CreateGame" && isset($_POST['userId']))
{
	$userId = get_post('userId');
	$gameId = CreateGame($db_server, $userId);
	
	AddUserToGame($db_server, $gameId, $userId);
	
	$PlanetList = Array();
	$planetAmount = 5;
	
	GeneratePlanets($db_server, $PlanetList, $gameId, $planetAmount);

	SetUsersToRandomPlanets($db_server, $PlanetList, $gameId);
	
	
	
}elseif ($functionName == "ConnectToGame" && isset($_POST['userId']) && isset($_POST['gameId']))
{
	$userId = get_post('userId');
	$gameId = get_post('gameId');
	
	AddUserToGame($db_server, $gameId, $userId);
	
}elseif ($functionName == "GeneratePlanets" && isset($_POST['gameId']) && isset($_POST['planetAmount']))
{
	$gameId = get_post('gameId');
	$planetAmount = get_post('planetAmount');
	
	$PlanetList = Array();
	
	GeneratePlanets($db_server, $PlanetList, $gameId, $planetAmount);

	SetUsersToRandomPlanets($db_server, $PlanetList, $gameId);
}elseif ($functionName == "StartGame" && isset($_POST['gameId']))
{
	$gameId = get_post('gameId');
	
	StartGame($db_server, $gameId);
}


GenrateGameStatusXML($gameId);

mysql_close($db_server);
exit;


function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}



function GenrateGameStatusXML($gameId)
{
	echo "<?xml version='1.0' encoding='UTF-8'?><root>";
	
	$sql="SELECT users.id, users.login
			FROM users_in_game
			LEFT JOIN users
			ON users_in_game.userId=users.id WHERE users_in_game.gameId = '$gameId'";
			
	$result=mysql_query($sql);

	echo "<players>";
	while($row = mysql_fetch_array($result))
	{
		echo "<player>";
		echo "<userId>".$row['id']."</userId>";
		echo "<userlogin>".$row['login']."</userlogin>";
		echo "</player>";
	}
	echo "</players>";
	
	$sql="SELECT * FROM `planets` WHERE gameId = '$gameId'";
	$result=mysql_query($sql);

	echo "<planets>";
	while($row = mysql_fetch_array($result))
	{
		echo "<planet>";
			echo "<planetNomer>".$row['planetNomer']."</planetNomer>";
			echo "<positionX>".$row['positionX']."</positionX>";
			echo "<positionY>".$row['positionY']."</positionY>";
			echo "<playerId>".$row['userId']."</playerId>";
			echo "<ships>".$row['ships']."</ships>";
			echo "<shipsPerTurn>".$row['shipsPerTurn']."</shipsPerTurn>";
			echo "<attackRate>".$row['attackRate']."</attackRate>";
		echo "</planet>";
	}
	echo "</planets>";
	
	
	
	$sql="SELECT `pending`,`creator_userId` FROM `games` WHERE Id = '$gameId'";
	$result=mysql_query($sql);
	echo "<gameStatus>";
	while($row = mysql_fetch_array($result))
	{
		if($row['pending'] == 1)
		{
			$pendingString = "true";
		}
		else
		{
			$pendingString = "false";
		}
		
		echo "<gameId>".$gameId."</gameId>";
		echo "<pending>".$pendingString."</pending>";
		echo "<creator_userId>".$row['creator_userId']."</creator_userId>";
	}
	echo "</gameStatus>";
	
	
	
	
	
	echo "</root>";
}

function SetUsersToRandomPlanets($db_server, &$planetList, $gameId)
{
	$sql="SELECT `userId` FROM `users_in_game` WHERE gameId = '$gameId'";
	$result=mysql_query($sql);
	
	$playerList = Array();

	while($row = mysql_fetch_array($result))
	{
		array_push($playerList, $row['userId']);
	}
	
	foreach($playerList as $player)
	{
		$result = false; 
		while(!$result)
		{
			$planetNomer = rand(0, sizeof($planetList)-1);
			$planet = $planetList[$planetNomer];
			if($planet->player == null)
			{
				$planet->player = $player;
				$result = true;
			}
		}
	}
	
	foreach($planetList as $planet)
	{
		if(!($planet->player == null))
		{
			$query = "UPDATE planets SET userId='$planet->player' WHERE planetNomer='$planet->PlanetNomer';";
			mysql_query($query, $db_server);
		}
	}	

}

function GeneratePlanets($db_server, &$PlanetList, $gameId, $planetAmount)
{
	$sql="DELETE FROM `planets` WHERE gameId = $gameId";
	$result=mysql_query($sql);
	
	$i = 1;
	while($i <= $planetAmount)
	{
		$result = false; 
		while(!$result)
		{
			$result = AddPlanet($PlanetList, rand(1, 10), rand(1, 10));
		}
		
		$i++;
	}
	
	$query = "";
	foreach($PlanetList as $planet)
	{
		$shipsPerTurn = 10 + rand(-5, 7);
		$attackRate = 1 + rand(-5, 6)/10;
		
		$query = "INSERT INTO `planets`(`gameId`, `planetNomer`, `positionX`, `positionY`, `ships`, `shipsPerTurn`, `attackRate`, `userId`) VALUES ($gameId, $planet->PlanetNomer,$planet->PositionX,$planet->PositionY,10,$shipsPerTurn,$attackRate,0);";
		mysql_query($query, $db_server);
		$planetNomer = $planetNomer + 1;
	}
}

function StartGame($db_server, $gameId)
{
	$query = "UPDATE games SET pending='0' WHERE id='$gameId'";
	$result = mysql_query($query, $db_server);
	if (!$result)
	{
		echo "<?xml version='1.0' encoding='UTF-8'?><error>INSERT failed</error>";
		exit;
	}
}

function CreateGame($db_server, $userId)
{
	$query = "INSERT INTO games (`pending`, `creator_userId`) VALUES (1, $userId)";
	$result = mysql_query($query, $db_server);
	if (!$result)
	{
		echo "<?xml version='1.0' encoding='UTF-8'?><error>INSERT failed</error>";
		exit;
	}
	
	$gameId = mysql_insert_id();
	
	return $gameId;
}

function AddUserToGame($db_server, $gameId, $userId)
{
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
		
		$sql="SELECT `planetNomer`, `userId` FROM `planets` WHERE gameId = '$gameId' && (userId = '0' || userId = '$userId')";
		$result=mysql_query($sql);
		
		$planetNomers = Array();

		while($row = mysql_fetch_array($result))
		{
			if (!($row['userId'] == $userId))
			{
				array_push($planetNomers, $row['planetNomer']);
			}
		}
		
		$randPlanetNomer = $planetNomers[rand(0, sizeof($planetNomers)-1)];
		
		$query = "UPDATE planets SET userId='$userId' WHERE planetNomer='$randPlanetNomer'";
		if (!mysql_query($query, $db_server))
		{
			echo "<?xml version='1.0' encoding='UTF-8'?><error>INSERT failed</error>";
			exit;
		}
		
	}
}

function AddPlanet(&$planetList, $x, $y)
{
	if(GetPlanet($planetList, $x, $y) == null)
	{
		$PlanetAmount = sizeof($planetList);
		array_push($planetList, new Planet($x, $y, $PlanetAmount));
		return true;
	}
	
	return false;
}

function GetPlanet($planetList, $x, $y)
{
	foreach($planetList as $planet)
	{
		//print_r($planet);
		
		if($planet->PositionX == $x && $planet->PositionY == $y)
		{
			return $planet;
		}		
	}
	
	return null;
}

class Planet{

	public $PositionX;
	public $PositionY;
	public $PlanetNomer;
	
	public $player;

	public function Planet($x, $y, $pnomer)
	{
		$this->PositionX = $x;
		$this->PositionY = $y;
		$this->PlanetNomer = $pnomer;
		
		$this->player = null;
	}
}

?>
