<?php
	$mysqli = new mysqli("mysql.hostinger.co.uk","u895445490_admin","conorianjamiethomas","u895445490_tsdb");

	/* Check for connection error */
    if (mysqli_connect_errno()) {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }

	$parentid = $_POST["parentid"];
	$name = $_POST["name"];
        $username = $_POST["username"];
	$password = $_POST["password"];
	
	/* Prepare an INSERT statement */
	$stmt = $mysqli->prepare("INSERT INTO children (parentid, name, username, password) VALUES (?,?,?,?)");
	$stmt->bind_param("isss", $parentid, $name, $username, $password);

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
