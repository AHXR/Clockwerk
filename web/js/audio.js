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
    This controls all the file paths. Easy adjustments.
*/
var
    a_sound_settings = {
        FOLDER: "sounds/",
        START: "start.wav",
        STOP: "end.wav",
        TRIGGER: "trigger.wav",
        WARNING: "warning.wav",
        MUTE: "silence.wav",
        UNMUTE: "unmute.wav"   
}

function playSoundFile(fileName) {
    if( !b_muted ) {
        var
            a_sound = new Audio(a_sound_settings["FOLDER"] + fileName);
    
        /*
            This is divided by 100 because the volume slider goes from 0-100, not 0 to 1.
        */
        a_sound.volume = $("#volume").slider("value") / 100;
        a_sound.play();
    }
}