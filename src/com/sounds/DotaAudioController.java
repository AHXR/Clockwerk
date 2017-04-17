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

//Imports.
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.themes.ClockwerkThemes;

/*
 * This class controls everything related to the audio that plays when certain
 * events are triggered. The audio that is played is threaded through the interface: Runnable.
 * 
 * @author AR
 * @version 1.0
 * 
 * public void run( );
 * 
 * public void muteSoundSwitch( );
 * 
 * public void playDotaSound( int sound_num );
 * 
 * public void changeVolume( float amount );
 * 
 * String returnAudioPath( int id );
 */
public class DotaAudioController implements Runnable {
	
	/*
	 * This variables are related to the ID argument in "playDotaSound".
	 * I organized these to avoid having any sort of variable confusion 
	 * in the future.
	 */
	public final int
		SOUND_START = 0,
		SOUND_END = 1,
		SOUND_TRIGGER = 2,
		SOUND_WARNING = 3,
		SOUND_MUTE = 4,
		SOUND_UNMUTE = 5;
	
	/*
	 * Static Variables.
	 * 
	 * @b_muted - Has the user muted this program?
	 * 
	 * @f_volume - What is the current volume of this program?
	 */
	static boolean
		b_muted;
	
	static float
		f_volume = -30; // Median between maximum gain and minimum gain is -37. (6, -80); default = 50
	
	/*
	 * This gave me a little trouble. It's very sloppy. The maximum gain you can use 
	 * in AudioInputStream is 6. The minimum gain is -80. Because I was using a GUI
	 * to adjust the volume via a scrollbar, I had to make it correlate with the volume.
	 * 
	 * The condition checks if the volume is <= 50. In both responses, the amount is subtracted
	 * by 37 because the median between 6 and -80 is -37. 
	 * 
	 * If the volume is <=50, it changes the volume by the amount subtract 43. 43 was the 
	 * golden number as it made it possible for the volume to go as low as possible without
	 * causing an error with the program. The same applied with 57. If the number was higher than 50
	 * then the number 57 was the golden number to prevent any issues.
	 * 
	 * Meh.
	 */
	public void changeVolume( float amount ) {
		f_volume = (amount <= 50) ? ( (amount - 43) - 37 ) : ( ( amount - 57 ) - 37 );
	}
	
	/*
	 * Function that muted the sound by a switch toggle. No parameters or anything like that. 
	 * The reason for the playDotaSound being in swapped locations in each different condition was 
	 * because of how playDotaSound is setup.
	 * 
	 * If everything was muted, nothing would play. So the user wouldn't here a mute sound if I had muted
	 * the program AFTER setting the variable.
	 */
	public void muteSoundSwitch( ) {
		if( !b_muted ) {
			playDotaSound( SOUND_MUTE );
			b_muted = true; 
		}
		else {
			b_muted = false;
			playDotaSound( SOUND_UNMUTE );
		}
	}
	
	/*
	 * All those const variables earlier that referred to the audio ID are translated through this function.
	 * This function then returns the path of the file after. DotaAudioFileSystem.java contains the default
	 * path of the files.
	 * 
	 * It was separated just so I don't have to go through all this code just to change a few things.
	 */
	String returnAudioPath( int id ) {
		
		String
			s_return = null;
		
		switch( id ) {
			case SOUND_START: {
				s_return = DotaAudioFileSystem.START.setting(); // "The gears are in motion"
				break;
			}
			case SOUND_END: {
				s_return = DotaAudioFileSystem.END.setting(); // "Must be getting rusty here"
				break;
			}
			case SOUND_TRIGGER: {
				s_return = DotaAudioFileSystem.TRIGGER.setting(); // "My gears turn"
				break;
			}
			case SOUND_WARNING: {
				s_return = DotaAudioFileSystem.WARNING.setting(); // "Hurry the hands of the clock"
				break;
			}
			case SOUND_MUTE: {
				s_return = DotaAudioFileSystem.MUTE.setting(); // "SILENCE"
				break;
			}
			case SOUND_UNMUTE: {
				s_return = DotaAudioFileSystem.UNMUTE.setting(); // "Speak your last"
				break;
			}
		}
		return DotaAudioFileSystem.DIR.setting() + s_return;
	}
	
	/*
	 * The function that does all the audio playing. I'm not going to lie, I had trouble putting this together.
	 * I received a lot of inspiration from help forums on how to play audio through AudioInputStream.
	 */
	public void playDotaSound( int sound_num ) {
		
		/*
		 * Refer to the note about muteSoundSwitch. It explains why b_muted is the first condition to even
		 * start this function. Also, it saves the hassle of this code running through all these different
		 * things just to say: "Oh wait, I'm muted".
		 */
		if( !b_muted ) {
			try{
				/*
				 * Here I used returnAudioPath to convert the path of the audio into a resource.
				 * Then the audio was converted into a file, which allowed me to break it into AudioInputStream.
				 */
				File
					s_file = new File( ClockwerkThemes.s_run_path + ClockwerkThemes.ThemeSettings.FOLDER.setting() + returnAudioPath( sound_num ) );
				
	            AudioInputStream ais = AudioSystem.getAudioInputStream( s_file );
	            Clip aud = AudioSystem.getClip();  	
	            aud.open(ais);
	            
	            /*
	             * The audio file is now open. Let's adjust the volume based on the changeVolume algorithm. 
	             */
	            FloatControl
	            	fc_volume = (FloatControl) aud.getControl( FloatControl.Type.MASTER_GAIN );
	            
	            fc_volume.setValue( f_volume );
	            
	            /*
	             * Without the thread being here, the audio would literally freeze the program until it was completed.
	             * This would cause the clock to be delayed and that would have been the biggest issue here. 
	             * 
	             * With the thread being implemented, it allowed the audio to smoothly run in the background after
	             * the user would trigger an event.
	             */
	            Thread t1 = new Thread(new Runnable() {
					@Override
					public void run() {
						aud.start();
						
						while (!aud.isRunning())
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
			            while (aud.isRunning())
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
			            aud.close();
					}
	            });
	            t1.start();
	            
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }
		}
	}

	@Override
	public void run() {
		
	}
}