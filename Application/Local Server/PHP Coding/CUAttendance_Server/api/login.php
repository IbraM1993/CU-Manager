<?php
$response = array();
try
{
    include_once 'db_functions.php';
    $data =  file_get_contents('php://input');
	file_put_contents('log/login input.txt', print_r($data , true));
    $params = json_decode($data, true);
    
    $df = new DB_Functions(basename(__FILE__, '.php'));   
    $test = array();
    $username = key_exists("username", $params) ? $params["username"] : "";
    $password = key_exists("password", $params) ? $params["password"] : "";
	$test = $df->login($username, $password);
    file_put_contents('log/login output.txt', print_r( json_encode($response), true));
    echo json_encode($test);
}
catch (Exception $e)
{
    $response["is_completed"]   = "false";
    $response["message"]        = $e->getMessage();
    echo json_encode($response);

}
?>
