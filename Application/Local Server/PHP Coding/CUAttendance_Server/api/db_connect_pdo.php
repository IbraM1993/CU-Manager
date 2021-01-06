<?php

class PDO_DB_CONNECT extends PDO{

    const PARAM_host    ='localhost';
    const PARAM_port    ='3306';
    const PARAM_db_pass ='mutapi123456';
    const PARAM_db_name ='mut_attendance';
    const PARAM_user    ='mutapi';

    public function __construct($options=null){

        parent::__construct('mysql:host='.PDO_DB_CONNECT::PARAM_host.';dbname='.PDO_DB_CONNECT::PARAM_db_name.';charset=utf8mb4',//;port='.MyPDO::PARAM_port.'
        PDO_DB_CONNECT::PARAM_user,
        PDO_DB_CONNECT::PARAM_db_pass, $options);
    }

    public function query($query){ //secured query with prepare and execute
        $args = func_get_args();
        array_shift($args); //first element is not an argument but the query itself, should removed

        $reponse = parent::prepare($query);
        $reponse->execute($args);
        return $reponse;

    }

    public function insecureQuery($query){ //you can use the old query at your risk ;) and should use secure quote() function with it
        return parent::query($query);
    }
}

?>