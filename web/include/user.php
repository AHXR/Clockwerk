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

require( "include/db.php" );

global 
    $SQL_CON;
 
function init( $debug=true ) {
    
    if( !$debug )
        error_reporting(0);
    
    $SQL_CON = mysqli_connect( $GLOBALS["SQL_HOST"], $GLOBALS["SQL_USER"], $GLOBALS["SQL_PASS"], $GLOBALS["SQL_DB"] );
    
    if ( !$SQL_CON )
        createJSVariable( "b_db_error", "true" );
    else {
        
        $b_clockcookie = isset( $_COOKIE[ "clockwerk" ] );
        
        if( $b_clockcookie )
            $SQL_RES = mysqli_query( $SQL_CON, "SELECT * FROM `user_settings` WHERE code='" . mysqli_real_escape_string( $SQL_CON, $_COOKIE[ "clockwerk" ] ) . "'") or die( mysqli_error( $SQL_CON ) );
        
        if( !isset( $SQL_RES ) || !$b_clockcookie ) {
            createJSVariable( "user_theme", "Clockwerk" );
            
            $SQL_NEW = mysqli_query( $SQL_CON, "INSERT INTO `user_settings` (code) VALUES('null')") or die( mysqli_error( $SQL_CON ));
            
            $s_id = mysqli_insert_id( $SQL_CON );
            $s_code = sha1( $s_id . $_SERVER['REMOTE_ADDR'] );
         
            $SQL_NEW = mysqli_query( $SQL_CON, "UPDATE `user_settings` SET code='" . $s_code . "' WHERE id=" . $s_id );
            setcookie( "clockwerk", $s_code, time() + 86400 * 30 );
        }
        else {
            $SQL_DATA = mysqli_fetch_assoc( $SQL_RES );
            
            // Updating the cookie so it will never expire.
            setcookie( "clockwerk", $SQL_DATA[ "code" ], time() + 86400 * 30 );
            
            // Loading user settings.
            echo('<script type="text/javascript">
            var a_user_data = {
                runes: "' . $SQL_DATA[ "runes" ] . '",
                camps: "' . $SQL_DATA[ "camps" ] . '",
                thirty: "' . $SQL_DATA[ "thirty" ] . '",
                fifteen: "' . $SQL_DATA[ "fifteen" ] . '",
                ontime: "' . $SQL_DATA[ "ontime" ] . '",
                toggleButton: "' . $SQL_DATA[ "toggleButton" ] . '",
                muteButton: "' . $SQL_DATA[ "muteButton" ] . '",
                theme: "' . $SQL_DATA[ "theme" ] . '"
            }
        </script>
                ');
        }
    }
}

function getUserSettings( $setting ) {
    return $USER_SETTINGS[ $setting ];
}

function createJSVariable( $var_name, $var_value ) {
    echo('<script type="text/javascript">var ' . $var_name . ' = "' . $var_value . '";</script>');
}
?>