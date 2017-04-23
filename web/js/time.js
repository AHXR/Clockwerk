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
    
    @i_time - Controls the current timer's value
    @t_timer - The timer itself.
*/
var 
    i_time = a_clock_settings["DEFAULT"];
var
    t_timer = setInterval(updateClock, a_clock_settings["INTERVAL"]);
        
/*
    updateClock is a very small function that is very similiar to what is found inside of TimerControlEvents.java
    All this does is trigger events depending on the user's settings.
*/
function updateClock() {
    
    if (b_timer_active) {
        /*
		 * 1000 = 1 second. 
		 * Adds to the variables.
		 */
        i_time -= 1000;

        switch (i_time) {
            case 0: // Times Up
                {
                    if (b_ontime)
                        playSoundFile(a_sound_settings["TRIGGER"])

                    resetTimerValues();
                    break;
                }
            case 15000: // 15 Seconds Left
                {
                    if (b_fifteen) 
                        playSoundFile(a_sound_settings["WARNING"])
                    break;
                }
            case 30000: // 30 Seocnds left
                {
                    if (b_thirty || b_runes)
                        playSoundFile(a_sound_settings["WARNING"])
                    break;
                }

            /*
                * These two cases check if the timer is 1:30 or 1:45. This is why both variables
                * b_runes and b_stacks are mentioned here.
            */
            case 75000:
                {
                    if (b_fifteen && b_runes && b_stacks)
                        playSoundFile(a_sound_settings["WARNING"])
                    break;
                }
            case 90000:
                {
                    if (b_thirty && b_runes && b_stacks)
                        playSoundFile(a_sound_settings["WARNING"])
                    break;
                }
        }
    }

    // Convert miliseconds to a readable time.
    $("#clock").text(formatTime(i_time));
}

/*
    This function simply formats the time into a presentable time for the user. 
    The trailing zero portion was added so the clock looks more fitting.
*/
function formatTime(mil) {
    var
        i_ms,
        i_sec,
        i_mins,
        s_sec;

    i_ms = mil % 1000;
    mil = (mil - i_ms) / 1000;

    i_sec = mil % 60;
    mil = (mil - i_sec) / 60;
    i_mins = mil % 60;

    i_sec == 0 ? s_sec = i_sec + "0" : s_sec = i_sec;

    return "0" + i_mins + ':' + s_sec;
}

/*
    Resets the timer value back to the default #.
*/
function resetTimerValues() {
    i_time = a_clock_settings["DEFAULT"];
}