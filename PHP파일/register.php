<?php

$name=$_POST["name"];
$arr = explode("/",$name);

$connect = mysqli_connect("localhost", "syoppo", "super1313!#!#", "syoppo");
mysqli_set_charset($connect, "utf-8");

if(!$connect) echo '접속실패';

$insertSQL="INSERT INTO member(eMail, pwd) values('$arr[0]','$arr[1]')";
mysqli_query($connect, $insertSQL);

mysqli_close($connect);
?>

