<?php

$name=$_POST["name"];
$arr = explode("/",$name);

$connect = mysqli_connect("localhost", "syoppo", "super1313!#!#", "syoppo");
mysqli_set_charset($connect, "utf-8");

if(!$connect) echo '접속실패';

$sqlSelect = "SELECT breakdown.bdName AS 구분내역, date, cash, memo, inputNum FROM input 
                INNER JOIN breakdown 
                ON breakdown.bdNum = input.bdNum
                WHERE eMail='$arr[0]'
                ORDER BY date DESC";


$rs = mysqli_query($connect, $sqlSelect);
while($row = mysqli_fetch_assoc($rs)){
    echo '#';
    echo $row["구분내역"];
    echo "/";
    echo $row["date"];
    echo "/";
    echo $row["memo"];
    echo "/";
    echo $row["cash"];
    echo "/";
    echo $row["inputNum"];
}

mysqli_close($connect);

?>