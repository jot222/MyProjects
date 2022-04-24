<?php
    function My_login($username, $password){
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }

        $sql_command = "SELECT user_name, full_name, interest, update_frequency FROM tb_users WHERE user_name='$username' and password='$password'";
        $result = mysqli_query($con_db, $sql_command);
        $num_rows = mysqli_num_rows($result);
        if($num_rows > 0){
            $row = mysqli_fetch_array($result);
            $username =  $row[0];
            $fullname = $row[1];
            $interest = $row[2];
            $update_frequency = $row[3];
            echo 'Success;' . $username . ';' . $fullname . ';' . $interest . ';' . $update_frequency;
        }
        else{
            echo 'Failed';
        }

        mysqli_close($con_db);
    }


    
    function My_register($username, $password, $fullName, $interest){
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }
        $sql_command = "INSERT INTO tb_users (user_name, password, full_name, interest, update_frequency, online) VALUES ('$username', '$password', '$fullName', '$interest', '10', CURRENT_TIMESTAMP)";
        $result = mysqli_query($con_db, $sql_command);
        $num_rows = mysqli_num_rows($result);

        if($result){
            echo "successful insert";
        }
        else{
            echo"failed insert";
        }
        mysqli_close($con_db);
    }
    

    function My_Update($username, $password, $fullName, $interest, $update_frequency, $oldUserName){        
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }
        
        $sql_command = "UPDATE tb_users SET user_name='$username', full_name='$fullName', interest='$interest', update_frequency='$update_frequency' WHERE user_name ='$oldUserName'";
        //echo "Query was " . $sql_command;

        $result = mysqli_query($con_db, $sql_command);
        $num_rows = mysqli_num_rows($result);

        if($result){
            echo 'SuccessfulUpdate;' . $username . ';' . $fullName . ';' . $interest . ';' . $update_frequency;
        }
        else{
            echo"failed update";
        }

        mysqli_close($con_db);
    }

    
    function My_search($searchTerm){   
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }

        $sql_command = "SELECT full_name, connected_mac FROM tb_users WHERE full_name LIKE '$searchTerm%'";
        $result = mysqli_query($con_db, $sql_command);
        $num_rows = mysqli_num_rows($result);

        if($num_rows > 0){
            echo $num_rows . ';';
            for($x=0; $x<$num_rows; $x++){
                $row = mysqli_fetch_array($result);
                $fullName =  $row[0];
                $connectedMac = $row[1];
                echo $fullName . ';' . $connectedMac . ';';
            }
        }
        else{
            echo"Search Empty";
        }
        mysqli_close($con_db);
    }

    function get_news($interest){   
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }

        if($interest == "Sciences(Default)"){
            $sql_command = "SELECT URL FROM tb_news WHERE category LIKE 'Sciences%'";
        }
        else{
            $sql_command = "SELECT URL FROM tb_news WHERE category LIKE '$interest%'";
        }

        //echo "Query is " . $sql_command;        
        
        $result = mysqli_query($con_db, $sql_command);
        $num_rows = mysqli_num_rows($result);

        
        if($num_rows > 0){
            echo $num_rows . ';';
            for($x=0; $x<$num_rows; $x++){
                $row = mysqli_fetch_array($result);
                $url =  $row[0];
                echo $url . ';';
            }
        }
        else{
            echo "News Search Empty";
        }
        
        mysqli_close($con_db);
    }
    
    function get_location($macAddress){
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }

        $sql_command = "SELECT building, floor FROM tb_location WHERE mac='$macAddress'";
        $result = mysqli_query($con_db, $sql_command);
        $num_rows = mysqli_num_rows($result);

        if($num_rows > 0){
            $row = mysqli_fetch_array($result);
            $building = $row[0];
            $floor = $row[1];
            echo "Success;" . $building . ";" . $floor;
        }
        else{
            echo"Failure";
        }
        mysqli_close($con_db);
    }

    function update_mac($username, $macAddress){
        $con_db = new mysqli("localhost:8889", "root", "root", "hw1_db");
        if(mysqli_connect_errno($con_db)){
            echo "Failed to connect to MYSQL: " . mysqli_connect_error();
        }
        $sql_command = "UPDATE tb_users SET connected_mac='$macAddress' WHERE user_name ='$username'";

        //echo "Query;".$sql_command;
        
        $result = mysqli_query($con_db, $sql_command);

        if($result){
            echo 'SuccessfulUpdate;' . $username . ';' . $macAddress . ';';
        }
        else{
            echo"failed update";
        }

        mysqli_close($con_db);
        

    }
    


    $method = $_POST['method'];
    switch($method){
        case 'login':
            $username = $_POST['username'];
            $password = $_POST['password'];
            My_login($username, $password);
            break;
        case 'register':
            $username = $_POST['username'];
            $password = $_POST['password'];
            $fullName = $_POST['fullName'];
            $interest = $_POST['interest'];
            My_register($username, $password, $fullName, $interest);
            break;       
        case 'update':
            $username = $_POST['username'];
            $password = $_POST['password'];
            $fullName = $_POST['fullName'];
            $interest = $_POST['interest'];
            $oldUserName = $_POST['oldUserName'];
            $update_frequency = $_POST['updateFreq'];
            My_update($username, $password, $fullName, $interest, $update_frequency, $oldUserName);
            break;
        case 'search':
            $searchTerm = $_POST['searchTerm'];
            My_search($searchTerm);
            break; 
        case 'convert':
            $macAddress = $_POST['macAddress'];
            get_location($macAddress);
            break;
        case 'news':
            $interest = $_POST['interest'];
            get_news($interest);
            break;
        case 'updateMac':
            $username = $_POST['username'];
            $macAddress = $_POST['macAddress'];
            update_mac($username, $macAddress);
            break;
        default:
            break;
    }

?>