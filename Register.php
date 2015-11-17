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
	
        $stmt2 = $mysqli->prepare("SELECT id FROM user WHERE username = ? AND password = ?");
        $stmt2->bind_param("ss", $username, $password);

        /* Execute the statement */
        $stmt2->execute();
    
        mysqli_stmt_store_result($stmt2);
        mysqli_stmt_bind_result($stmt2, $id);
	
        while(mysqli_stmt_fetch($stmt2)){
            $user["id"] = $id;
        }
    
        /* Close Statement */
        $stmt2->close();
	
        echo json_encode($user);
    
        /* Close Connection */
        $mysqli->close();

?>										
