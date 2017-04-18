package clockwerk.global;
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
 * This enumeration holds the default values for the shortcut keys.
 */
public enum GlobalKeyShortcuts {
	TOGGLE		("F5"),
	MUTE		("F6"),
	DEBUG		("Num Lock");
	
	private String
		settings;
	
	GlobalKeyShortcuts( String settings ) { this.settings = settings; }
	public String settings( ) { return settings; }
	
	/*
	 * This function was created so that the user is allowed to save their shortcut
	 * key bindings.
	 */
	public void changeValue( String newValue ) {
		this.settings = newValue;
	}
}
