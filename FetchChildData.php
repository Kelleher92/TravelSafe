<?php

    $mysqli = new mysqli("mysql.hostinger.co.uk","u895445490_admin","conorianjamiethomas","u895445490_tsdb");

    /* Check for connection error */
    if (mysqli_connect_errno()) {
        printf("Connect failed: %s\n", mysqli_connect_error());
        exit();
    }

    $parentid = $_POST["parentid"];

    $stmt = $mysqli->prepare("SELECT * FROM children WHERE parentid = ?");
    $stmt->bind_param("i", $parentid);

    /* Execute the statement */
    $stmt->execute();
    
    mysqli_stmt_store_result($stmt);
    mysqli_stmt_bind_result($stmt, $id, $parentid, $name, $username, $password);
	
    while(mysqli_stmt_fetch($stmt)){
        $child["id"] = $id;
        $child["parentid"] = $parentid;
        $child["name"] = $name;
        $child["username"] = $username;
        $child["password"] = $password;
    }
    
    /* Close Statement */
    $stmt->close();
	
    echo json_encode($child);
    
    /* Close Connection */
    $mysqli->close();

?>											
