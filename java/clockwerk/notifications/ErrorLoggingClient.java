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

package clockwerk.notifications;

// Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;

/*
 * ErrorLogging is a light class that simply allows the program to write
 * any errors into a file. The file appending method was inspired by mkyong's tutorial on
 * appending content to a file.
 * 
 * public static void log( String message )
 * 
 * @author AR
 * @version 1.0
 */
public class ErrorLoggingClient {
	
	/*
	 * @ERROR_PATH - The path where the file is stored.
	 */
	public static final String ERROR_PATH = "error.txt";
	
	/*
	 * This function attempts to read or create the ERROR_PATH file. If it doesn't exist,
	 * it will create the file itself. After, it will append text.
	 */
	public static void log( String message ) {
		FileWriter f_write = null;
		BufferedWriter b_write = null;
		
		try {
			
			/*
			 * Creating a new file instance and checking if it's valid or not. Regardless
			 * of the result, a file will be appended.
			 */
			File
				f_log = new File( ERROR_PATH );
			
			if( !f_log.exists() ) 
				f_log.createNewFile();
			
			f_write = new FileWriter( f_log.getAbsoluteFile(), true );
			b_write = new BufferedWriter( f_write );
			
			/*
			 * Getting the date as a string before completely writing to the file.
			 */
			DateFormat 
				date_f = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
			
			Date date = new Date( );
			
			/*
			 * Writing to the file and closing both the FileWriter and BufferedWriter after.
			 */
			b_write.write( "[" + date_f.format( date ) + "] " + message + "\r\n" );
		} catch (IOException e) {
			e.printStackTrace();
			NotificationDialogClient.Error("Clockwerk Error", "There was an issue adding an error into your " + ERROR_PATH + " file.");
		} finally {
			try {
				b_write.close();
				f_write.close();
			} catch (IOException e) {
				NotificationDialogClient.Error("Clockwerk Error", "There was an internal issue while processing your error file.");
				e.printStackTrace();
			}
		}
	}
}
