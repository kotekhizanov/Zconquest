<?php
require_once 'logindb.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

mysql_select_db($db_database, $db_server)
or die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

if (isset($_POST['login']) && isset($_POST['password']))
{
	$login = get_post('login');
	$password = get_post('password');
	
	if(isset($_POST['signup']))
	{
		$sql="SELECT * FROM users WHERE login='$login'";
		$result=mysql_query($sql);

		// Mysql_num_row is counting table row
		$count=mysql_num_rows($result);

		// If result matched $myusername and $mypassword, table row must be 1 row
		if(!$count==0)
		{
			// username taken
			echo "<?xml version='1.0' encoding='UTF-8'?><error>Username already taken</error>";
			exit();
		}
		
		$query = "INSERT INTO users (`login`, `password`) VALUES('$login', '$password')";
		if (!mysql_query($query, $db_server))
		{
			echo "<?xml version='1.0' encoding='UTF-8'?><error>Sign up failed</error>";
		}
	}
	
	$sql="SELECT `id` FROM `users` WHERE login = '$login' && password = '$password'";
	$result=mysql_query($sql);
	
	$count=mysql_num_rows($result);
	if($count==0)
	{
		// username taken
		echo "<?xml version='1.0' encoding='UTF-8'?><error>Username and password are incorrect</error>";
		exit();
	}

	while($row = mysql_fetch_array($result))
	{
		$PlayerId = $row['id'];
	}
	
	echo "<?xml version='1.0' encoding='UTF-8'?>
		<player>
			<PlayerId>$PlayerId</PlayerId>
			<PlayerName>$login</PlayerName>
		</player>";
}

mysql_close($db_server);

function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}
?>