<?php

$name=$_POST["name"];
$arr = explode("/",$name);

$connect = mysqli_connect("localhost", "syoppo", "super1313!#!#", "syoppo");
mysqli_set_charset($connect, "utf-8");

if(!$connect) echo '접속실패';
$updateSQL="DELETE FROM input WHERE eMail='$arr[0]' AND inputNum='$arr[1]'";

mysqli_query($connect, $updateSQL);

mysqli_close($connect);
?>

