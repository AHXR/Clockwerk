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
    Variables
    
    @i_changing - This variable is important for when the user is changing his shortcut key.
        0 - No shortcut key is currently being changed.
        1 - Toggle key is currently being changed.
        2 - Mute/Unmute key is currently being changed.
*/
var
    i_changing = 0;

/*
    Here, the code checks if the right keys are being pressed.
*/
$(document).keydown(function(e) {
    e.preventDefault();
    
    switch( i_changing ) {
        case 0: {
            switch( e.which ) {
                case a_key_settings["TOGGLE"]: { // The user has toggled the clock on/off.
                    (!b_timer_active) ? playSoundFile(a_sound_settings["START"]): playSoundFile(a_sound_settings["STOP"]);
                    b_timer_active = !b_timer_active;
                    break;
                }
                case a_key_settings["MUTE"]: { // The user wants to mute the clock.
                    if( !b_muted ) {
                        playSoundFile(a_sound_settings["MUTE"]);
                        b_muted = true;
                    }
                    else {
                        b_muted = false;
                        playSoundFile(a_sound_settings["UNMUTE"]);
                    } 
                    break;
                }
            } 
            break;
        } 
        case 1: { // Toggle shortcut key set.
            a_key_settings["TOGGLE"] = e.which;
            endShortcutDia( );
            break;
        }
        case 2: { // Mute shortcut key set.
            a_key_settings["MUTE"] = e.which;
            endShortcutDia( );
            break;
        }
    }      
});
