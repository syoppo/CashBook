<?php

$name=$_POST["name"];
$arr = explode("/",$name);

$connect = mysqli_connect("localhost", "syoppo", "super1313!#!#", "syoppo");
mysqli_set_charset($connect, "utf-8");

$sqlSearch = "SELECT breakdown.bdName AS 구분내역, date, cash, memo, inputNum FROM input 
                INNER JOIN breakdown 
                ON breakdown.bdNum = input.bdNum
                WHERE eMail='$arr[0]' AND (('구분내역'='$arr[1]') OR (date='$arr[1]') OR (memo='$arr[1]') OR (cash='$arr[1]'))
                ORDER BY date";


$rs = mysqli_query($connect, $sqlSearch);
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