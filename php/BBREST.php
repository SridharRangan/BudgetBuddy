<?php
  $dbhost = 'localhost';
  $dbuser = 'budget_app_user';
  $dbpass = 'warhammer40k';
  $dbname = 'budget_buddy_app';
  $restkey = 'abc123xyz';

  if($_GET['auth'] != $restkey){
    http_response_code(401);
    exit('Invalid key');
  }

  $connection = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);
  if(!$connection){
    http_response_code(502);
    die('Connect Error (' . mysqli_connect_errno() . ') '
            . mysqli_connect_error());
  }

  $returnObj = new stdClass();
  $returnObj->retval = false;
  $returnObj->message = "default message";

  if(isset($_GET['tz'])){
    $tz = mysqli_real_escape_string($connection, $_GET['tz']);
    $query = "SET time_zone = $tz";
    mysqli_query($connection, $query);
  }

  mysqli_set_charset( $connection, 'utf8');


  switch($_GET['type']){

    case "login":
      $user = mysqli_real_escape_string($connection, $_GET['user']);
      $pass = mysqli_real_escape_string($connection, $_GET['pass']);
      $query = "SELECT * FROM Users WHERE user = '$user' AND pw = BINARY '$pass'";
      if(strlen($user) > 20){
        $returnObj->retval = false;
        $returnObj->message = "username too long";
        break;
      }
      $qry_result = mysqli_query($connection, $query);
      if($qry_result->num_rows == 1){
        $returnObj->retval = true;
        $returnObj->message = "Successful Login Attempt";
        $returnObj->data = mysqli_fetch_assoc($qry_result);
        //set timezone from user data
        $tz = $returnObj->data['tz'];
      }else if($qry_result->num_rows == 0){
        $returnObj->retval = false;
      }else{
        $returnObj->retval = false;
        $returnObj->message = "default case login failure";
      }
      break;

    case "newuser":
      $user = mysqli_real_escape_string($connection, $_GET['user']);
      $pass = mysqli_real_escape_string($connection, $_GET['pass']);
      $tz = mysqli_real_escape_string($connection, $_GET['tz']);
      if(isset($_GET['email'])){
        $email = mysqli_real_escape_string($connection, $_GET['email']);
      }
      if(strlen($user) > 20){
        $returnObj->retval = false;
        $returnObj->message = "username too long";
      }
      $query = "SELECT * FROM Users WHERE user = '$user'";
      $qry_result = mysqli_query($connection, $query);
      if($qry_result->num_rows == 1){
        $returnObj->retval = false;
        $returnObj->message = "Username taken";
      }else if($qry_result->num_rows == 0){
        if(isset($_GET['email'])){
          $query = "INSERT INTO Users (user, pw, tz, email) ";
          $query = $query . "VALUES ('$user', '$pass', '$tz', '$email')";
        }else{
          $query = "INSERT INTO Users (user, pw, tz) ";
          $query = $query . "VALUES ('$user', '$pass', '$tz')";
        }
        $qry_result = mysqli_query($connection, $query);
        $query = "SELECT * FROM Users WHERE user = '$user' AND pw = BINARY '$pass'";
        $qry_result = mysqli_query($connection, $query);
        $returnObj->data = mysqli_fetch_assoc($qry_result);
        $returnObj->retval = true;
      }else{
        $returnObj->retval = false;
        $returnObj->message = "default case";
      }
      break;

    default:
      $returnObj->retval = false;
      $returnObj->message = "ERROR: no type specified";
      break;
  }
  //for debugging
  //$returnObj->query = $query;
  $returnJSON = json_encode($returnObj);
  echo $returnJSON;
  mysqli_close($connection);
?>
