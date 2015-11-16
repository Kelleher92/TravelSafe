<?php

    $mysqli = new mysqli("mysql.hostinger.co.uk","u895445490_admin","conorianjamiethomas","u895445490_tsdb");

    /* Check for connection error */
    if (mysqli_connect_errno()) {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }

    $username = $_POST["username"];
    $password = $_POST["password"];

    $stmt = $mysqli->prepare("SELECT * FROM user WHERE username = ? AND password = ?");
    $stmt->bind_param("ss", $username, $password);

    /* Execute the statement */
    $stmt->execute();
    
    mysqli_stmt_store_result($stmt);
    mysqli_stmt_bind_result($stmt, $id, $emailaddress, $username, $password);
	
    while(mysqli_stmt_fetch($stmt)){
        $user["id"] = $id;
        $user["emailaddress"] = $emailaddress;
        $user["username"] = $username;
        $user["password"] = $password;
    }
    
    /* Close Statement */
    $stmt->close();
	
    echo json_encode($user);
    
    /* Close Connection */
    $mysqli->close();

?>											
