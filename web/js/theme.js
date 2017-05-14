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
 
 /*
    Here are where all the themes are located. The format is simple:
    ["Name of Theme", "Background Location"]
    
    The sounds are automatically picked from the folder in "sounds" with the
    same theme name. Therefore the Crystal Maiden theme has her sounds located
    in the "Crystal Maiden folder"
 */
var a_themes = [
    ["Clockwerk", "images/default.jpg"],
    ["Crystal Maiden", "images/cm.jpg"],
    ["Invoker", "images/invoker.jpg"]
];

/*
    This function scans through the a_themes array and see if the
    selected theme matches anything that was found.
    
    The silent parameter is used whether to show a notification or not.
*/
function changeTheme( themeName, silent=0 ) {
    for( var i = 0; i < a_themes.length; i ++) {
        
        if( a_themes[ i ][ 0 ] == themeName ) {
            $("body").css("background", "url('" + a_themes[ i ][ 1 ] + "')");
            a_sound_settings.FOLDER = "sounds/" + a_themes[ i ][ 0 ] + "/";
            break;
        }
    }
   
    if( !silent )
        showNotification( "You have changed your theme to '" + themeName + "'" );
}