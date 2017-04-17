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

package com.themes;
// Imports.
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.ini4j.Wini;

import com.sounds.DotaAudioFileSystem;

/*
 * ClockwerkThemes is a class that controls anything related to the program
 * and user changing the theme. Themes are stored in INI files and can easily
 * be modified.
 * 
 * This will allow the user to change the wallpaper and sounds of this program.
 * 
 * @author AR
 * @version 1.0
 */
public class ClockwerkThemes {
	
	/*
	 * The theme settings are located in this enumeration here. Considering enumerations 
	 * are predefined constants, I wanted to avoid using the simple variable statement
	 * methods I used in previous versions.
	 */
	public enum ThemeSettings {
		FOLDER 			( "/themes/" ),
		EXTENSION 		( "ini" ),
		SECTION 		( "theme" ),
		WALLPAPER 		( "background" ),
		CLOCK 			( "clock" ),
		GAMECLOCK 		( "gameclock" ),
		SOUNDS 			( "sounds" );
	
		private String 
			setting;
		
		ThemeSettings( String setting ) { this.setting = setting; }	
		public final String setting( ) { return setting; }
	}
	
	/*
	 * Static Variables:
	 * 
	 * @s_wallpaper_path - Retrieved wallpaper path from INI.
	 * 
	 * @s_clock_code - Retrieved clock color code from INI.
	 * 
	 * @s_game_clock_code - Retrieved game clock color code from INI
	 * 
	 * @s_custom_sounds - Retrieved sounds path from INI
	 * 
	 * @b_theme_reset - Does the theme need to be reset?
	 */
	public static String 
		s_wallpaper_path,
		s_clock_code,
		s_game_clock_code,
		s_custom_sounds,
		s_run_path;

	public static boolean
		b_theme_reset;
	
	/*
	 * Load theme will fetch the values from the INI file and store it inside of the static
	 * variables. Once that is processed, it will then set the b_theme_reset value to true.
	 * This change will trigger an event found on the timer in ClockwerkGUI.
	 */
	public static void loadTheme( String ThemeName ) throws IOException {
		Wini 
			f_ini;
		f_ini = new Wini( new File( ClockwerkThemes.s_run_path + ClockwerkThemes.ThemeSettings.FOLDER.setting() + ThemeName ) );

		String
			s_section = ThemeSettings.SECTION.setting();
		
		// Loading values.
		s_wallpaper_path = f_ini.get( s_section, ThemeSettings.WALLPAPER.setting(), String.class );
		s_clock_code = f_ini.get( s_section, ThemeSettings.CLOCK.setting(), String.class );
		s_game_clock_code = f_ini.get( s_section, ThemeSettings.GAMECLOCK.setting(), String.class );
		
		// Changing the audio directory.
		DotaAudioFileSystem.DIR.changeValue( f_ini.get( s_section, ThemeSettings.SOUNDS.setting(), String.class ) );
		
		// Setting theme result switch.
		b_theme_reset = true;
	}
}
