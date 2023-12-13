<?php
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < 13; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    move_uploaded_file($_FILES["file"]["tmp_name"], "folder/".$randomString);
?>