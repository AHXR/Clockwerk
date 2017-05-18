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
	 * 
	 * @i_key_change - Allows ShortcutKeyGUI to know what key the client is changing.
	 */
	private static boolean 
		debug = false;
	private static String
		s_text = "", s_debug_key = GlobalKeyShortcuts.DEBUG.settings(), s_startstop_key = GlobalKeyShortcuts.TOGGLE.settings(), s_mute_key = GlobalKeyShortcuts.MUTE.settings();
	private static int
		i_key_change;
	public static boolean[ ][ ]
		b_combo_keys = new boolean[ 2 ][ 3 ],
		b_combo_pressed = new boolean[ 2 ][ 3 ];
	
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
		
		// This will let the program check whether the client might perform a combo.
		updateComboStatus( s_text, true );
		
		if( ( getComboStatus( 0 ) ) && ( ( debug && s_text.equals( s_debug_key ) ) || ( !debug && s_text.equals( s_startstop_key ) ) || ( !debug && s_text.equals( GlobalKeyShortcuts.TOGGLE.settings( ) ) ) ) ) {		
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
		if( ( getComboStatus( 1 ) ) && s_text.equals( s_mute_key ) ) {
			MasterControlSound dac = new MasterControlSound( );
			dac.muteSoundSwitch( );
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		s_text = NativeKeyEvent.getKeyText(e.getKeyCode());
		// This will let the program know the combo event isn't gonna happen.
		updateComboStatus( s_text, false );
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
		// Setting the i_key_change variable so ShortcutKeyGUI can access it.
		i_key_change = keyid;
		
		// Creating and showing the dialog.
		ShortcutKeyGUI sk_gui = new ShortcutKeyGUI( );
		sk_gui.lblText.setText("<html>Type the key you would like to change this shortcut to. Your current keys: <br /><br />Start/Stop: '" + GlobalKeyShortcuts.TOGGLE.settings() + "'<br />Mute: '" + GlobalKeyShortcuts.MUTE.settings() + "'</html>");
		
		switch( keyid ) {
			case 0: {
				sk_gui.txtKey.setText( GlobalKeyShortcuts.TOGGLE.settings() );
				
				if( b_combo_keys[ 0 ][ 0 ] )
					sk_gui.chckbxShift.setSelected( true );
				
				if( b_combo_keys[ 0 ][ 1 ] )
					sk_gui.chckbxCtrl.setSelected( true );
				
				if( b_combo_keys[ 0 ][ 2 ] )
					sk_gui.chckbxAlt.setSelected( true );
				break;
			}
			case 1: {
				sk_gui.txtKey.setText( GlobalKeyShortcuts.MUTE.settings() );
				
				if( b_combo_keys[ 1 ][ 0 ] )
					sk_gui.chckbxShift.setSelected( true );
				
				if( b_combo_keys[ 1 ][ 1 ] )
					sk_gui.chckbxCtrl.setSelected( true );
				
				if( b_combo_keys[ 1 ][ 2 ] )
					sk_gui.chckbxAlt.setSelected( true );
				break;
			}
		}
		
		sk_gui.setVisible( true );
	}
	
	/*
	 * This function gives ShortcutKeyGUI access to the i_key_change function.
	 */
	public static int getKeyChange( ) {
		return i_key_change;
	}
	
	/*
	 * updateComboBasedOnKey is called in ShortcutKeyGUI. The purpose of this function is to 
	 * quickly update the b_combo_keys array.
	 */
	public static void updateComboBasedOnKey( boolean shift, boolean ctrl, boolean alt ) {
		b_combo_keys[ i_key_change ][ 0 ] = shift;
		b_combo_keys[ i_key_change ][ 1 ] = ctrl;
		b_combo_keys[ i_key_change ][ 2 ] = alt;
	}
	
	/*
	 * This will update the current combo status based on the results from
	 * nativeKeyPressed and nativeKeyReleased
	 */
	private void updateComboStatus( String charcode, boolean val ) {
		if( charcode.equals( "Shift" ) ) {
			if( b_combo_keys[ 0 ][ 0 ] ) // If the toggle-shift combo key is toggled on/off.
				b_combo_pressed[ 0 ][ 0 ] = val;
			
			if( b_combo_keys[ 1 ][ 0 ] ) // If the mute-shift combo key is toggled on/off.
				b_combo_pressed[ 1 ][ 0 ] = val;
		}
		
		if( charcode.equals( "Ctrl" ) ) {
			if( b_combo_keys[ 0 ][ 1 ] ) // If the toggle-ctrl combo key is toggled on/off.
				b_combo_pressed[ 0 ][ 1 ] = val;
			
			if( b_combo_keys[ 1 ][ 1 ] ) // If the mute-ctrl combo key is toggled on/off.
				b_combo_pressed[ 1 ][ 1 ] = val;
		}
		
		if( charcode.equals( "Alt" ) ) {
			if( b_combo_keys[ 0 ][ 2 ] ) // If the toggle-alt combo key is toggled on/off.
				b_combo_pressed[ 0 ][ 2 ] = val;
			
			if( b_combo_keys[ 1 ][ 2 ] ) // If the mute-alt combo key is toggled on/off.
				b_combo_pressed[ 1 ][ 2 ] = val;
		}
	}
	
	/*
	 * Returns the combo status. If the combo key is enabled but the combo key is NOT pressed 
	 * then return false.
	 * 
	 * This means when the ShortCutKeyListener is active, it will not activate the shortcut key
	 * event unless all the combo keys are together. Examples being CTRL+ALT+F5 or SHIFT+CTRL+F7
	 */
	private boolean getComboStatus( int keyCode ) {
		for( int i = 0; i <= 2; i ++ ) {
		//	System.out.print( b_combo_keys[ keyCode ][ i ] + " | " + b_combo_pressed[ keyCode ][ i ] + "\r\n" );
			if( b_combo_keys[ keyCode ][ i ] == true && b_combo_pressed[ keyCode ][ i ] == false )
				return false;
		}
		
		return true;
	}
}
