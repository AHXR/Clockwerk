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

package com.sounds;

/*
 * Very basic class. This class controls all the related sound file paths. I organized this
 * just so I can quickly adjust any filename changes.
 * 
 * @author AR
 * @version 1.1
 */
public enum DotaAudioFileSystem {
	DIR 		("/sounds/"),
	START		("start.wav"),
	END			("end.wav"),
	TRIGGER		("trigger.wav"),
	WARNING		("warning.wav"),
	MUTE		("silence.wav"),
	UNMUTE		("unmute.wav");
	
	private String 
		setting;
	
	DotaAudioFileSystem( String setting ) { this.setting = setting; }
	public String setting( ) { return setting; }
	
	/*
	 * This function was created so that the user can set the sound directory
	 * to where they choose.
	 */
	public void changeValue( String newValue ) {
		this.setting = newValue;
	}
}
