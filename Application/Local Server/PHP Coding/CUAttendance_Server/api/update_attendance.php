<?php
$response = array();
try
{
    include_once 'db_functions.php';
    $data =  file_get_contents('php://input');
    $params = json_decode($data, true);
    
    $df = new DB_Functions(basename(__FILE__, '.php'));   
    $test = array();
    $id = key_exists("id", $params) ? $params["id"] : 0;
	$test = $df->updateAttendance($id);
    //file_put_contents('log/update result.txt', print_r(json_encode($test, JSON_NUMERIC_CHECK), true));
    
    $response["is_completed"]   = "true";
    $response["int_info"]       = $test;
    echo json_encode($response, JSON_NUMERIC_CHECK);
}
catch (Exception $e)
{
    $response["is_completed"]   = "false";
    $response["message"]        = $e->getMessage();
    echo json_encode($response, JSON_NUMERIC_CHECK);

}
?>
