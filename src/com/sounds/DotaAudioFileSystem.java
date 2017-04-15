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
 * @version 1.0
 */
public class DotaAudioFileSystem {
	
	public final String directoryPrefix = "/com/sounds/";
	
	public final String audioStart = "start.wav";
	
	public final String audioEnd = "end.wav";
	
	public final String audioTrigger = "trigger.wav";
	
	public final String audioWarning = "warning.wav";
	
	public final String audioMute = "silence.wav";
	
	public final String audioUnmute = "unmute.wav";
}
