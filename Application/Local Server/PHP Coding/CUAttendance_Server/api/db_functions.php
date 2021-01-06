<?php
error_reporting(0);
class db_functions {

    private static $POST_HOT_THRESHOLD_DATE;
    private static $POST_HOT_THRESHOLD_VALUE;
    private $db;
    private $methodeCall;
    private $callId;
    // define("DATE_FORMAT","%Y/%m/%d %H:%i:%s");
    // LATEST UPDATE 20160125
    // constructor

    function __construct($callerName) {
        include_once 'db_connect_pdo.php';
        include 'config.php';
        // connecting to database
       //file_put_contents('log/__construct.txt', print_r($callerName, true));
        $options = array();
        $options[PDO::MYSQL_ATTR_INIT_COMMAND] ='SET time_zone = \'+00:00\'';

        $this->db = new PDO_DB_CONNECT($options);
        $this->db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_OBJ);
        //file_put_contents('log/__construct Time Zone.txt', print_r(DEFAULT_TIME_ZONE, true));
        date_default_timezone_set(DEFAULT_TIME_ZONE);
        $this->callId = mt_rand();
        $this->methodeCall = $callerName;
        // $this->db->connect();
    }
    // destructor
    function __destruct() {
        $timeStart = $_SERVER["REQUEST_TIME_FLOAT"];
        $time = microtime(true) - $timeStart;
       //file_put_contents("log/Call Log ".$this->methodeCall.".txt", print_r("\n ".$this->methodeCall."'".($this->callId * 1000)."'$time ms", true),//file_APPEND);
    }

    function multiexplode ($delimiters, $string) {

        $ready = str_replace($delimiters, $delimiters[0], $string);
        $launch = explode($delimiters[0], $ready);
//         $launch = preg_split($delimiters[0], $ready ,PREG_SPLIT_NO_EMPTY);
        return  $launch;
    }

    function placeholders($text, $count = 0, $separator = ",") {
    
        $result = array();
        if ($count > 0) {
            for ($x = 0; $x < $count; $x ++) {
                $result[] = $text;
            }
        }
    
        return implode($separator, $result);
    }
    
    function startsWith($haystack, $needle) {
        $length = strlen($needle);
        return (substr($haystack, 0, $length) === $needle);
    }

    function endsWith($haystack, $needle) {
        $length = strlen($needle);
        if ($length == 0) {
            return true;
        }
        return (substr($haystack, -$length) === $needle);
    }
	
	function insertRowData($testTableArray){
	    try {
	    if(!empty($testTableArray)) {
	            $datafields = array("name", "phone", "mut_id");
	            $insert_values = array();
	            $question_marks = array();
	            foreach($testTableArray as $d){
	                $temp = array();
	                $temp["name"]      = $d["name"];
	                $temp["phone"]     = $d["phone"];
	                $temp["mut_id"]    = $d["mut_id"];
	                $question_marks[] = '('  . $this->placeholders('?', sizeof($temp)) . ')';
	                $insert_values = array_merge($insert_values, array_values($temp));
	            }
	            $sql = "INSERT INTO `test_table` (" . implode(",", $datafields ) . ") VALUES " . implode(',', $question_marks);
	            $result = $this->db->prepare ($sql);
	            $result->execute($insert_values);
	            if(!empty($result->errorInfo()[1]))
	            {
	                $response["message"] = $result->errorInfo()[2];
	                $this->db->rollBack();
	                return $response;
	            }
	        }
			return 1;
        } catch(PDOException $ex) {
            $this->db->rollBack();
        }
        return 0;
	}
	
	function getAllData(){
	    $data = array();
	    $sqlResult = $this->db->query("select * from `test_table`");
	    $result = $sqlResult->fetchAll(PDO::FETCH_ASSOC);
	    foreach ($result as $r) {
	        $data[] = array(
	            'id' => $r["id"],
	            'name' => $r["name"],
	            'phone' => $r["phone"],
	            'mut_id' => $r["mut_id"]
	        );
	    }
	    return $data;
	}

	function getClasses($id) {
	    
	    $sql = "SELECT 
	        `class`.`id`, `class`.`code`, `class`.`room`, `class`.`instructor_id`
	        , `class`.`start_time`, `class`.`end_time`
	        , `class`.`mon`, `class`.`tue`, `class`.`wed`, `class`.`thu`, `class`.`fri`, `class`.`sat` 
	        , count(*) as `students` 
	        FROM `class` 
	        INNER JOIN `class_student` on `class_student`.`class_id` = `class`.`id` WHERE `class`.`instructor_id` = ? 
	           Group by `class`.`code`";
	    //file_put_contents("log/get_classes.txt", print_r($sql." id : ".$id , true));
        $result = $this->db->prepare($sql);
	    $result->execute(array($id));
	    $response = array();
	    if($result->rowCount() > 0 ){
	        $R = $result->fetchAll(PDO::FETCH_ASSOC);
	        foreach ($R as $row) {
	            $response[]  = $row;
	        }
	    }
	    return $response;
	}

	function login($username, $password){
	    $sql = "SELECT `id`, `name`, `email`, `phone` FROM `instructors` where `email` = ? and `password` = ? ";
	    $result = $this->db->prepare($sql);
	    $result->execute(array($username, $password));
	    
	    if($result->rowCount() > 0 ){
	        $R = $result->fetchAll(PDO::FETCH_ASSOC);
	        return $R[0];
	    }
	    return "";
	}

	function getClassAttendance ($classId, $date) {
	    //file_put_contents("log/getClassAttendance.txt", print_r(" Class Id : ".$classId." Date : ".$date , true));
	    
	    $sql = "SELECT `attendance`.`id` 	             as 'id'
                        , `class_student`.`class_id` 	 as 'class_id'
                        , `class_student`.`student_id` 	 as 'student_id'
                        , `attendance`.`status` 		 as 'status'
                        , `attendance`.`comment` 		 as 'comment'
                        , `students`.`name` 		     as 'student_name'
                        , `students`.`image` 		     as 'student_icon'
                        , `students`.`tag_id` 		     as 'student_tag'
                        FROM `class_student` 
	                    Inner Join `students` on `students`.`id` = `class_student`.`student_id`
                        Left JOIN `attendance` 
                            on `attendance`.`class_id` = `class_student`.`class_id` 
                            and `attendance`.`student_id` = `class_student`.`student_id` 
                            and `attendance`.`class_date` = ?
                        where `class_student`.`class_id` = ?";
	    //file_put_contents("log/getClassAttendance sql .txt", print_r($sql , true));
	    $result = $this->db->prepare($sql);
	    $result->execute(array($date, $classId));
	    $response = array();
	    //file_put_contents("log/getClassAttendance result .txt", print_r($result , true));
	    //file_put_contents("log/getClassAttendance result count .txt", print_r($result->rowCount() , true));
	    if($result->rowCount() > 0 ){
	        $R = $result->fetchAll(PDO::FETCH_ASSOC);
	        foreach ($R as $row) {
	           $response[] = $row;
	        }
	    }
	    return $response;
	}

	function startClass($id) {
	    $dat = date('Y-m-d');
	    $sqlTest = "Select * from `attendance` where `class_id` = ? and `class_date` = ?";
	    $result = $this->db->prepare($sqlTest);
	    $result->execute(array($id, $dat));
	    $count = $result->rowCount();
	    //file_put_contents("log/startClass sql .txt", print_r($sqlTest, true));
	    //file_put_contents("log/startClass count .txt", print_r($count, true));
	    if($count == 0){
	        $sql = "INSERT INTO `attendance`(`class_id`, `student_id`, `class_date`, `status`, `comment`)
                SELECT `class_student`.`class_id`, `class_student`.`student_id`, NOW(), 0, ''
                FROM `class_student` WHERE `class_student`.`class_id` = ?";   
	        $result = $this->db->prepare($sql);
	        $result->execute(array($id));
	    }
	    return $this->getClassAttendance($id, $dat);
	    
	}
	
	function updateAttendance($id){
	    $sql = "Update `attendance` set `status` = 1 where `id` = ?";
	    $result = $this->db->prepare($sql);
	    $result->execute(array($id));
	    return $result->rowCount();
	}
}
?>

