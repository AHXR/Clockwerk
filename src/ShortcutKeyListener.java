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

// Imports
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import clockwerk.global.GlobalKeyShortcuts;

/*
 * ShortcutKeyListener is a class that worked with NativeKeyListener to check any keys
 * pressed from the user. In ClockwerkGUI.java, there is a small block of code that
 * allows this instance to run.
 * 
 * This class is extremely important because it allows the user to toggle his shortcuts
 * outside of the program. Meaning he can activate the program while in Dota. Without this,
 * the user would have to alt-tab to start the timer.
 * 
 * public void showShortcutDialogue( int keyid )
 */
public class ShortcutKeyListener implements NativeKeyListener {

	/*
	 * Static variables.
	 * 
	 * @debug - When I first built this program, I started off with the timer before anything.
	 * This is nothing to worry about tbh.
	 * 
	 * @s_text - This variable is used to store the value of the key pressed on the user's keyboard.
	 * 
	 * @s_debug_key - My debug key for when @debug was true.
	 * 
	 * @s_startstop_key - Stores the start/stop key.
	 * 
	 * @s_mute_key - Stores the mute key.
	 */
	private static boolean 
		debug = false;
	private static String
		s_text = "", s_debug_key = GlobalKeyShortcuts.DEBUG.settings(), s_startstop_key = GlobalKeyShortcuts.TOGGLE.settings(), s_mute_key = GlobalKeyShortcuts.MUTE.settings();
	
	/*
	 * This event is triggered the moment the user presses a key.
	 */
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		
		/*
		 * Any key that is pressed on the keyboard instantly gets stored into the s_text
		 * variable. Then checks the conditions after.
		 */
		s_text = NativeKeyEvent.getKeyText(e.getKeyCode());
		
		if( ( debug && s_text.equals( s_debug_key ) ) || ( !debug && s_text.equals( s_startstop_key ) ) ) {
			
			/*
			 * The start/stop key has now been pressed. The program now calls upon the ClockwerkTimer
			 * class and the DotaAudioController class to adjust the proper variables and start/stop the timer.
			 */
			TimerControlEvents cwt = new TimerControlEvents( );
			MasterControlSound dac = new MasterControlSound( );
				
			if( !cwt.isTimerActive() )
				dac.playDotaSound( dac.SOUND_START );
			else 
				dac.playDotaSound( dac.SOUND_END );
			
			TimerControlEvents.setTimerStatus( !cwt.isTimerActive() );
		}
		
		/*
		 * Mute key and the proper adjustments afterwards.
		 */
		if( s_text.equals( s_mute_key ) ) {
			MasterControlSound dac = new MasterControlSound( );
			dac.muteSoundSwitch( );
		}
	}

	/*
	 * Other events I didn't touch.
	 */
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
	
	
	/*
	 * This is the very basic and light function that allows the user to change the shortcut based
	 * on their keyboard input. 
	 * 
	 * At first, I wanted to make this work where the user had to just press a key and it would immediately
	 * bind it to the variable. But then I wanted to put non-English keyboards into consideration.
	 */
	public void showShortcutDialogue( int keyid ) {
		JFrame gui_shortcut = new JFrame( "Change Shortcut" );
	    String s_key = JOptionPane.showInputDialog( gui_shortcut, "Type the key you would like to change this shortcut to. Your current keys: \r\n\r\nStart/Stop: '" + GlobalKeyShortcuts.TOGGLE.settings() + "'\r\nMute: '" + GlobalKeyShortcuts.MUTE.settings() + "'");

	    /*
	     * The key needs to be a proper value. If it's nothing or the user decided to cancel the shortcut change, nothing will happen.
	     */
	    if( s_key != null ) {
	    	switch( keyid ) {
	    		case 0: {
	    			GlobalKeyShortcuts.TOGGLE.changeValue( s_key );
	    			ClientSettingsControl.saveSettings();
	    			break;
	    		}
	    		
	    		case 1: {
	    			GlobalKeyShortcuts.MUTE.changeValue( s_key );
	    			ClientSettingsControl.saveSettings();
	    			break;
	    		}
	    	}
	    }
	}
}
