<?php
require_once 'logindb.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);

if (!$db_server) die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect");

mysql_select_db($db_database, $db_server)
or die("<?xml version='1.0' encoding='UTF-8'?><error>Unable to connect</error>");

if (isset($_POST['login']) && isset($_POST['password']))
{
	$login = get_post('login');
	$password = get_post('password');
	
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

	if (mysql_query($query, $db_server))
	{
		$PlayerId = mysql_insert_id();
		echo "<?xml version='1.0' encoding='UTF-8'?>
			<player>
				<PlayerId>$PlayerId</PlayerId>
				<PlayerName>$login</PlayerName>
			</player>";
	}
	else
	{
		echo "<?xml version='1.0' encoding='UTF-8'?><error>Sign up failed</error>";
	}
}

mysql_close($db_server);

function get_post($var)
{
	return mysql_real_escape_string($_POST[$var]);
}
?>