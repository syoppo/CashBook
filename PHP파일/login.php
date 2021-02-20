<?php

$name=$_POST["name"];
$arr = explode("/",$name);

$connect = mysqli_connect("localhost", "syoppo", "super1313!#!#", "syoppo");
mysqli_set_charset($connect, "utf-8");

$sqlSelect = "SELECT *FROM member WHERE (eMail='$arr[0]' AND pwd='$arr[1]')";
$rs = mysqli_query($connect, $sqlSelect);
while($row = mysqli_fetch_array($rs)){
    echo $row["eMail"];
    echo "/";
    echo $row["pwd"];
    echo "/";
    echo "true";
    echo "/";
}
?>