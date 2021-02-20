<?php

$name=$_POST["name"];
$arr = explode("/",$name);

$connect = mysqli_connect("localhost", "syoppo", "super1313!#!#", "syoppo");
mysqli_set_charset($connect, "utf-8");

if(!$connect) echo '접속실패';
$updateSQL="UPDATE input SET date='$arr[1]', memo='$arr[2]', cash='$arr[3]' WHERE eMail='$arr[0]' AND inputNum='$arr[4]'";

mysqli_query($connect, $updateSQL);

mysqli_close($connect);
?>

