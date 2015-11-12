<?php
	$mysqli = new mysqli("mysql.hostinger.co.uk","u895445490_admin","conorianjamiethomas","u895445490_tsdb");

	/* Check for connection error */
    if (mysqli_connect_errno()) {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }

        $username = $_POST["username"];
	$emailaddress = $_POST["emailaddress"];
	$password = $_POST["password"];
	
	/* Prepare an INSERT statement */
	$stmt = $mysqli->prepare("INSERT INTO user (username, password, emailaddress) VALUES (?,?,?)");
	$stmt->bind_param("sss", $username, $password, $emailaddress);

	/* Execute the statement */
	$stmt->execute();
	/* Close Statement */
	$stmt->close();	
	
	$response = array();
	$response[id] = 00;
	$response[type] = TRUE;

	echo json_encode($response);
	
	/* Close Connection */
	$mysqli->close();
?>			
