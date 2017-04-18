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
import java.io.File;
import java.io.IOException;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import clockwerk.global.GlobalFileUser;
import clockwerk.global.GlobalKeyShortcuts;
import clockwerk.notifications.NotificationDialogClient;

/*
 * This is class allows the program to load and save settings. The enumeration
 * GlobalFileUser controls most of the settings related to this class. Refer to
 * GlobalFileUser.java for more information.
 * 
 * By default, the file format is saved in ini. The library used is ini4j, which is also
 * used in the theme system in Clockwerk. 
 * 
 * ini4j - The Java .ini library - http://ini4j.sourceforge.net
 * 
 * @author AR
 * @version 1.0
 * 
 * public static void loadSettings( );
 */
public class ClientSettingsControl {
	
	/*
	 * The loadSettings function goes through a journey of checking
	 * whether the file exist or not, then doing the necessary procedures
	 * to make sure the user can have their own settings file.
	 * 
	 * I created the settings function within Clockwerk for the sake of the
	 * user not having to constantly change their theme or shortcuts every time
	 * the program is launched.
	 */
	public static void loadSettings( ) {
		File 
			f_settings = new File( GlobalFileUser.FILE.settings() );
		Wini
			f_ini;
		String
			s_sec = GlobalFileUser.SECTION.settings();
			
		/*
		 * Here is where loadSettings checks if the file exists or not. If it
		 * doesn't, it will create a file for the user and set everything to the
		 * default settings.
		 */
		if( !f_settings.exists() ) {
			
			/*
			 * Attempting to create a new file since it obviously doesn't exist.
			 * Can't write into a file that doesn't exist, y'know?
			 */
			try {
				f_settings.createNewFile();

				// The writing begins. Everything gets set to the default value.
				f_ini = new Wini( f_settings );
				f_ini.put( s_sec, GlobalFileUser.BOUNTY.settings(), "1");
				f_ini.put( s_sec, GlobalFileUser.CAMPS.settings(), "1");
				f_ini.put( s_sec, GlobalFileUser.WARNINGS.settings(), "1-1-1");
				f_ini.put( s_sec, GlobalFileUser.VOLUME.settings(), "50");
				f_ini.put( s_sec, GlobalFileUser.TOGGLE.settings(), GlobalKeyShortcuts.TOGGLE.settings() );
				f_ini.put( s_sec, GlobalFileUser.MUTE.settings(), GlobalKeyShortcuts.MUTE.settings() );
				f_ini.put( s_sec, GlobalFileUser.THEME.settings(), "default.ini");
				f_ini.store( );
				
				TimerControlEvents.setWarningSignal( 0, true );
				TimerControlEvents.setWarningSignal( 15, true );
				TimerControlEvents.setWarningSignal( 30, true );
				
				GlobalThemeControl.loadTheme("default.ini");
				
			} catch (InvalidFileFormatException e) {
				e.printStackTrace();
				NotificationDialogClient.Error( "Settings", "There was an internal file format issue when trying to create your settings file.");
			} catch (IOException e) {
				e.printStackTrace();
				NotificationDialogClient.Error( "Settings", "There was an internal issue trying to create your settings file.");	
			}
		}
		else {
			
			/*
			 * Now, if the settings file DOES exist ... the program is simply going to load 
			 * the user's settings which will adjust everything in the GUI. The adjustments
			 * can be found on SwingGUI.java.
			 */
			try {
				f_ini = new Wini( f_settings );
				
				// Loading both the bounty and camps toggle. Since both functions require a boolean, the values are converted.
				TimerControlEvents.setBountyRunesToggle( f_ini.get( s_sec, GlobalFileUser.BOUNTY.settings() ).equals("1") ? true : false );
				TimerControlEvents.setCampStacksToggle( f_ini.get( s_sec, GlobalFileUser.CAMPS.settings() ).equals("1") ? true : false );
				
				/*
				 * The settings file saved the warning signals in a different way. Each value was split between
				 * a delimiter. 
				 * 
				 * In this block of code, I'm fetching the whole value from the .ini and then
				 * breaking it down using the split function. Each split value is assigned to the s_warnings
				 * array.
				 * 
				 * Note to self: The pipe delimiter will not work with split unless specified. 
				 */
				String[ ] s_warnings = f_ini.get( s_sec, GlobalFileUser.WARNINGS.settings( ) ).split( "-" );
				
				/*
				 * Just as a reference outside of TimerControlEvents.java:
				 * 		0 - On Time
				 * 		15 - 15 Seconds Warnings.
				 * 		30 - 30 Seconds Warnings.
				 */
				TimerControlEvents.setWarningSignal( 0, s_warnings[ 0 ].equals("1") ? true : false );
				TimerControlEvents.setWarningSignal( 15, s_warnings[ 1 ].equals("1") ? true : false );
				TimerControlEvents.setWarningSignal( 30, s_warnings[ 2 ].equals("1") ? true : false );
			
				/*
				 * The volume is now retrieved from the .ini file. Due to the algorithm I used 
				 * in MasterControlSound.java, I wanted to avoid having any sort of complications
				 * from the user who chooses to adjust their settings via the file itself.
				 * 
				 * Not to mention it would be pointless for me to reverse the algorithm just because
				 * of how I was being lazy and didn't want to make an absolute value from the volume
				 * setting.
				 * 
				 * This is why i_volume_abs was added in a later version of clockwerk.java.
				 */
				int
					i_volume = Integer.parseInt( f_ini.get( s_sec, GlobalFileUser.VOLUME.settings() ) );
				
				MasterControlSound.changeVolume( i_volume );
				MasterControlSound.i_volume_abs = i_volume;
				
				// Shortcuts are being retrieved here.
				GlobalKeyShortcuts.TOGGLE.changeValue( f_ini.get( s_sec, GlobalFileUser.TOGGLE.settings() ) );
				GlobalKeyShortcuts.MUTE.changeValue( f_ini.get( s_sec, GlobalFileUser.MUTE.settings() ) );
				
				// Finally, the theme is being loaded based on the user's settings.
				GlobalThemeControl.loadTheme( f_ini.get( s_sec, GlobalFileUser.THEME.settings() ) );
			} catch (InvalidFileFormatException e) {
				e.printStackTrace();
				NotificationDialogClient.Error( "Settings", "There was an internal file format issue when trying to load your settings file.");
			} catch (IOException e) {
				e.printStackTrace();
				NotificationDialogClient.Error( "Settings", "There was an internal issue trying to load your settings file.");	
			}
		}
	}
	
	/*
	 * The saveSettings function goes through a shorter journey of just 
	 * storing the values from throughout the other classes.
	 */
	public static void saveSettings( ) {
		File 
			f_settings = new File( GlobalFileUser.FILE.settings() );
		Wini
			f_ini;
		String
			s_sec = GlobalFileUser.SECTION.settings();
		
		try {
			f_ini = new Wini( f_settings );
			
			/*
			 * At first, I actually was planning on using Boolean.toString and other sorts of 
			 * conversion methods just for this to work but then I realized the ternary conditional
			 * operator just made it way easier.
			 */
			f_ini.put( s_sec, GlobalFileUser.BOUNTY.settings(), TimerControlEvents.b_bountyrunes ? "1" : "0" );
			f_ini.put( s_sec, GlobalFileUser.CAMPS.settings(), TimerControlEvents.b_campstacks ? "1" : "0" );
			
			/*
			 * Here is where we use the operator again to build that delimiter setting to
			 * store in the ini file.
			 */
			String
				s_warnings;
			
			s_warnings = TimerControlEvents.b_onTime ? "1-" : "0-";
			s_warnings += TimerControlEvents.b_fifteen ? "1-" : "0-";
			s_warnings += TimerControlEvents.b_thirty ? "1" : "0";
			
			f_ini.put( s_sec, GlobalFileUser.WARNINGS.settings(), s_warnings );
			
			// Saving the rest of the values.
			f_ini.put( s_sec, GlobalFileUser.VOLUME.settings(), MasterControlSound.i_volume_abs );
			f_ini.put( s_sec, GlobalFileUser.TOGGLE.settings(), GlobalKeyShortcuts.TOGGLE.settings() );
			f_ini.put( s_sec, GlobalFileUser.MUTE.settings(), GlobalKeyShortcuts.MUTE.settings() );
			f_ini.put( s_sec, GlobalFileUser.THEME.settings(), GlobalThemeControl.s_theme_name );
			f_ini.store( );
			
		} catch( InvalidFileFormatException e ) {
			e.printStackTrace();
			NotificationDialogClient.Error( "Settings", "There was an internal issue trying to saving your settings file.");	
		} catch ( IOException e ) {
			e.printStackTrace();
			NotificationDialogClient.Error( "Settings", "There was an internal issue trying to saving your settings file.");	
		}
	}
}
