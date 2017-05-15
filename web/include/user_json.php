<?php

/*
 * Clockwerk: Dota 2 Rune and Camp Stacking Timer.
 * Copyright (C) 2017 AR.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

header('Content-Type: application/json');
require( "db.php" );

$aJaxResult;

$SQL_CON = mysqli_connect( $GLOBALS["SQL_HOST"], $GLOBALS["SQL_USER"], $GLOBALS["SQL_PASS"], $GLOBALS["SQL_DB"] );

if( $_POST[ "functionname" ] == "saveSettings") {
    
    $USER_DATA = $_POST["arguments"][ 0 ];
    
    $SQL_RES = mysqli_query( $SQL_CON, "UPDATE `user_settings` SET 
        runes = " . $USER_DATA["runes"] . ",
        camps = " . $USER_DATA["camps"] . ",
        thirty = " . $USER_DATA["thirty"] . ",
        fifteen = " . $USER_DATA["fifteen"] . ",
        ontime = " . $USER_DATA["ontime"] . ",
        toggleButton = " . $USER_DATA["toggleButton"] . ",
        muteButton = " . $USER_DATA["muteButton"] . ",
        theme = '" . $USER_DATA["theme"] . "'
        WHERE code='" . mysqli_real_escape_string( $SQL_CON, $_COOKIE["clockwerk"] ) . "' LIMIT 1");

    if( !$SQL_RES ) 
        $aJaxResult = 'Internal Error';
    else
        $aJaxResult = "Updated";
}

echo json_encode( $aJaxResult );
?>