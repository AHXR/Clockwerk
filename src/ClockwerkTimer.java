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

// Imports.
import java.util.concurrent.TimeUnit;
import com.sounds.DotaAudioController;

/*
 * ClockwerkTimer is used in nearly all the other Java files. This class controls
 * everything related to time management. 
 * 
 * @author AR
 * @version 1.0
 * 
 * public boolean isTimerActive( );
 * 
 * public void setTimerStatus( boolean b_status );
 * 
 * public void setBountyRunesToggle( boolean b_status );
 * 
 * public void setCampStacksToggle( boolean b_status );
 * 
 * public void setWarningSignal( int w, boolean toggle );
 * 
 * public String FormatTime( int m );
 * 
 * public void updateTimerValues( );
 * 
 * public void resetTimerValues( boolean gameTime );
 */
public class ClockwerkTimer {
	
	/*
	 * Static Variables
	 * 
	 * @b_timer_status - Timer active or no?
	 * 
	 * @b_onTime - "On Time" setting toggle.
	 * 
	 * @b_thirty - "30 Seconds" setting toggle.
	 * 
	 * @b_fifteen - "15 Seconds" setting toggle.
	 * 
	 * @i_time - Main timer.
	 * 
	 * @i_game - Game timer.
	 * 
	 * @b_bountyrunes - Bounty Runes setting toggle.
	 * 
	 * @b_campstacks - Camp Stacks settings toggle.
	 */
	static boolean
		b_timer_status,
		b_onTime,
		b_thirty,
		b_fifteen;
	static int
		i_time = 120000,
		i_game;
	static boolean
		b_bountyrunes = true,
		b_campstacks = true;
	
	/*
	 * @DotaAudioController.java
	 */
	DotaAudioController dac = new DotaAudioController( );
	
	/*
	 * This starts the set of simple timer functions that can be
	 * called throughout the program and in different classes.
	 * 
	 * @isTimerActive - Is timer active?
	 * 
	 * @setTimerStatus - Sets b_timer_status
	 * 
	 * @setBountyRunesToggle - Sets b_bountyrunes
	 * 
	 * @setCampStacksToggle - Sets b_campstacks
	 * 
	 * @setWarningSignal - Sets the trigger warning toggle.
	 * 		w: 0 - b_onTime | 1 - b_thirty | 2 - b_fifteen
	 */
	public boolean isTimerActive( ) {
		return b_timer_status;
	}
	
	public void setTimerStatus( boolean b_status ) {
		b_timer_status = b_status;
	}
	
	public void setBountyRunesToggle( boolean b_status ) {
		b_bountyrunes = b_status;
	}
	
	public void setCampStacksToggle( boolean b_status ) {
		b_campstacks = b_status;
	}
	
	public void setWarningSignal( int w, boolean toggle ) {
		switch( w ) {
			case 0: {
				b_onTime = toggle;
				break;
			}
			case 15: {
				b_fifteen = toggle;
				break;
			}
			case 30: {
				b_thirty = toggle;
				break;
			}
		}

	}
	
	/*
	 * This will format the time of the clock so it shows up as a proper stopwatch on the GUI.
	 */
	public String FormatTime( int m ) {
		return String.format( "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes( m ), TimeUnit.MILLISECONDS.toSeconds( m ) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes( m ) ) );
	}
	
	/*
	 * updateTimerValues will add more to the i_time and i_game variable. Then it will work accordingly
	 * based on what the current timer value is.
	 */
	public void updateTimerValues( ) {
		
		/*
		 * 1000 = 1 second. 
		 * Adds to the variables.
		 */
		i_time -= 1000;
		i_game += 1000;
		
		switch( i_time ) {
			case 0: { // Times Up
				if( b_onTime ) 
					dac.playDotaSound( dac.SOUND_TRIGGER );
				
				resetTimerValues( false );
				break;
			}
			case 15000: { // 15 Seconds Left
				if( b_fifteen )
					dac.playDotaSound( dac.SOUND_WARNING );
				break;
			}
			case 30000: { // 30 Seconds Left
				if( b_thirty || b_bountyrunes )
					dac.playDotaSound( dac.SOUND_WARNING );
				break;
			}
			
			/*
			 * These two cases check if the timer is 1:30 or 1:45. This is why both variables
			 * b_bountyrunes and b_campstacks are mentioned here.
			 */
			case 75000: {
				if( b_fifteen && b_bountyrunes && b_campstacks )
					dac.playDotaSound( dac.SOUND_WARNING );
				break;
			}
			case 90000: {
				if( b_thirty && b_bountyrunes && b_campstacks )
					dac.playDotaSound( dac.SOUND_WARNING );
				break;
			}
		}
	}
	
	/*
	 * Resets timer values back to the beginning.
	 */
	public void resetTimerValues( boolean gameTime ) {
		i_time = 120000;
		
		if( gameTime )
			i_game = 0;
	}
}