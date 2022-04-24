<?php


//$input_utterance = 'Im so stressed about this exam';
 $input_utterance = $_POST['userMsg'];


$witRoot = "https://api.wit.ai/message?";
$witVersion = '20211002';




//echo "Post:" . "<br>" . "$input_utterance" . "<br>";

$witURL = $witRoot . "v=" . $witVersion . "&q=" . urlencode($input_utterance);

$ch = curl_init();
$header = array();
$header[] = 'Authorization: Bearer JIVECPMHDWRHIEF27UCXZ2J7OWTHO5FN';

curl_setopt($ch, CURLOPT_URL, $witURL);
curl_setopt($ch, CURLOPT_HTTPHEADER,$header); 
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); 

curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);



$server_output = curl_exec($ch); 
curl_close ($ch);  

//echo "<br>";
//echo "Response:";

$server_decoded_rsp = json_decode($server_output)->entities->{"issues:issues"};

$response = "";

for ($i = 0; $i < count($server_decoded_rsp); $i++){
	$keyword = $server_decoded_rsp[$i]->value;
	$con_db = mysqli_connect("localhost:8889", "root", "root", "hw2_witai");
  	if (mysqli_connect_errno($con_db)) {
    	echo "Failed to connect  to MYSql:" . mysqli_connect_error();
  	}
  	$sql_command = "SELECT answer FROM response WHERE keyword = '{$keyword}'";
  	$result = mysqli_query($con_db, $sql_command);
  	$num_rows = mysqli_num_rows($result);
  	if ($num_rows > 0) {
    	$row = mysqli_fetch_array($result);
    	$answer = $row[0];
    	echo $answer . ';';
  	} else {
    	echo "failed";
  	}

  	mysqli_close($con_db);
}



?>
