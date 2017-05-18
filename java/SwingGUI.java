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
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import clockwerk.notifications.NotificationDialogClient;
import javax.swing.JSlider;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

/*
 * This GUI was built through Eclipse's WindowBuilder (https://eclipse.org/windowbuilder/). 
 * There were a few adjustments made within the generated code that has been noted accordingly.
 */
public class SwingGUI extends JFrame {
	
	private static final long serialVersionUID = -2400434487677545157L;
	private JPanel contentPane;
	private boolean isLoadingTheme = true;

	static SwingGUI frame;
	
	// main()
	public static void main(String[] args) {
		Path
			crp = Paths.get( "" );

		GlobalThemeControl.s_run_path = crp.toAbsolutePath().toString();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Prepares the GUI.
				try {
					/*
					 * Settings are loaded before the frame gets created.
					 * This is to prevent the default settings from overriding 
					 * the user's settings.
					 */
					ClientSettingsControl.loadSettings( );
					frame = new SwingGUI();
				} catch (Exception e) {
					NotificationDialogClient.Error("Clockwerk", "There was an error while trying to create the GUI.");
					e.printStackTrace();
				}
				
				/*
				 * Here is where the jnativehook comes to use. This block attempts to
				 * register global key listening. This will allow the program to detect
				 * what keys are being pressed even without the application being focused.
				 * 
				 * This was used for shortcuts. Without this, the user would have to alt-tab
				 * and press the shortcut key on the program. Instead, they can be in-game and press the 
				 * shortcut key and the event will be triggered.
				 * 
				 * This registration process was taken from the jnativehook example
				 * https://github.com/kwhat/jnativehook/tree/master/src/java/org/jnativehook/example
				 */
				try {
					GlobalScreen.registerNativeHook();
				} catch (NativeHookException ex) {
					NotificationDialogClient.Error("Clockwerk", "An internal error has occurred. This application cannot be run.");
					
					System.err.println("There was a problem registering the native hook.");
		            System.err.println(ex.getMessage());

		            System.exit(1);
				}
				
				GlobalScreen.addNativeKeyListener(new ShortcutKeyListener());
				Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
				logger.setLevel(Level.WARNING);
				logger.setUseParentHandlers(false);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SwingGUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(SwingGUI.class.getResource("/clockwerk/images/icon.png")));
		setResizable(false);
		/*
		 * Here is where I create a new instance called "cwt" which stands for (C)lock(W)erk(T)imer.
		 * This instance was used to call functions within the class found inside "ClockwerkTimer.java".
		 * 
		 * Any sort of change to the user's settings directly called a function that would change
		 * how the timer would react. Simple stuff.
		 */
		TimerControlEvents cwt = new TimerControlEvents( );
		
		/*
		 * The skt instance (<S>hortcut<K>ey<L>istener) was implemented here to simply 
		 * allow the user to change their shortcuts via the shortcut dialogue. 
		 * 
		 * Inside the settings, they will be allowed to change what keys to use to start/stop the timer.
		 * They also have the option to mute/unmute the program.
		 */
		ShortcutKeyListener skt = new ShortcutKeyListener( );
		
		/*
		 * GUI Creation Starts here.
		 * Built with Eclipse's WindowBuilder (https://eclipse.org/windowbuilder/).  
		 */
		setOpacity(1.0f);
		setTitle("Dota 2 - On The Clockwerk");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 671, 399);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTime = new JLabel("00:00");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Piston Pressure", lblTime.getFont().getStyle(), 120));
		lblTime.setBounds(177, 112, 352, 138);
		contentPane.add(lblTime);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 665, 21);
		contentPane.add(menuBar);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JCheckBoxMenuItem chckbxmntmBountyRunes = new JCheckBoxMenuItem("Bounty Runes");
		chckbxmntmBountyRunes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimerControlEvents.setBountyRunesToggle( chckbxmntmBountyRunes.isSelected( ) );
				ClientSettingsControl.saveSettings();
			}
		});
		mnSettings.add(chckbxmntmBountyRunes);
		
		JCheckBoxMenuItem chckbxmntmCampStacks = new JCheckBoxMenuItem("Camp Stacks");
		chckbxmntmCampStacks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimerControlEvents.setCampStacksToggle( chckbxmntmCampStacks.isSelected() );
				ClientSettingsControl.saveSettings();
			}
		});
		mnSettings.add(chckbxmntmCampStacks);
		
		JMenu mnWarnings = new JMenu("Warnings");
		mnSettings.add(mnWarnings);
		
		JCheckBoxMenuItem chckbxmntmSeconds = new JCheckBoxMenuItem("30 Seconds");
		chckbxmntmSeconds.setSelected(true);
		chckbxmntmSeconds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TimerControlEvents.setWarningSignal(30, chckbxmntmSeconds.isSelected() );
				ClientSettingsControl.saveSettings();
			}
		});
		mnWarnings.add(chckbxmntmSeconds);
		
		JCheckBoxMenuItem chckbxmntmSeconds_1 = new JCheckBoxMenuItem("15 Seconds");
		chckbxmntmSeconds_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimerControlEvents.setWarningSignal(15, chckbxmntmSeconds_1.isSelected() );
				ClientSettingsControl.saveSettings();
			}
		});
		chckbxmntmSeconds_1.setSelected(true);
		mnWarnings.add(chckbxmntmSeconds_1);
		
		JCheckBoxMenuItem chckbxmntmOnTime = new JCheckBoxMenuItem("On Time");
		chckbxmntmOnTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TimerControlEvents.setWarningSignal(0, chckbxmntmOnTime.isSelected() );
				ClientSettingsControl.saveSettings();
			}
		});
		chckbxmntmOnTime.setSelected(true);
		mnWarnings.add(chckbxmntmOnTime);
		
		JMenu mnVolume = new JMenu("Volume");
		mnSettings.add(mnVolume);
		
		JSlider volumeSlider = new JSlider();
		volumeSlider.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				MasterControlSound.changeVolume( volumeSlider.getValue() );
				ClientSettingsControl.saveSettings();
			}
		});
		mnVolume.add(volumeSlider);
		
		JMenu mnShortcuts = new JMenu("Shortcuts");
		mnSettings.add(mnShortcuts);
		
		JMenuItem mntmStartstop = new JMenuItem("Start/Stop");
		mntmStartstop.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				skt.showShortcutDialogue( 0 );
			}
		});
		mnShortcuts.add(mntmStartstop);
		
		JMenuItem mntmMute = new JMenuItem("Mute");
		mntmMute.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				skt.showShortcutDialogue( 1 );
			}
		});
		mnShortcuts.add(mntmMute);
		
		JMenuBar menuBar_2 = new JMenuBar();
		mnSettings.add(menuBar_2);
		
		JMenuBar menuBar_1 = new JMenuBar();
		mnSettings.add(menuBar_1);
		
		JMenu mnThemes = new JMenu("Themes");
		menuBar.add(mnThemes);
		
		JLabel lblGameTime = new JLabel("0:00");
		lblGameTime.setVisible(false);
		lblGameTime.setForeground(Color.WHITE);
		lblGameTime.setFont(new Font("Piston Pressure", lblGameTime.getFont().getStyle(), 20));
		lblGameTime.setBounds(10, 314, 447, 36);
		contentPane.add(lblGameTime);
		
		/*
		 * GUI Creation Ends. But now we're going to load some themes dynamically based on what's inside of the "themes" folder.
		 * This program gathers all the file names inside of "themes" and creates menus out of them.
		 */	
		File f_themes_folder = null;
		f_themes_folder = new File( GlobalThemeControl.s_run_path + GlobalThemeControl.ThemeSettings.FOLDER.setting() );
		
		/*
		 * With all the files now collected. This program is going to start building menus out of them.
		 * The files are going to be compacted into f_theme_names and mntmMenuThemes is going to be
		 * used to store all the menus created by this retrieval process..
		 */
		File[ ] f_theme_names = f_themes_folder.listFiles( );
		JMenuItem[ ] mntmMenuThemes = new JMenuItem[ f_theme_names.length ];
			
		for( int i = 0; i < f_theme_names.length; i ++ ) {
			/*
			 * Getting the extension of the file.
			 */
			String
				f_ext = null,
				f_filename;
			int
				f_idx;
				
			f_filename = f_theme_names[ i ].getName( );
			f_idx = f_filename.lastIndexOf( '.' );
				
			if( f_filename.lastIndexOf( '.' ) > 0 ) {
				f_ext = f_filename.substring( f_idx + 1);
			}
				
			// DEBUG
			// System.out.print( f_filename + " | " + f_ext + "\r\n" );
				
			/*
			 * The item in the directory must be a file before it's even listed.
			 * Any sort of other files will break the process.
			 */
			if( f_theme_names[ i ].isFile( ) && f_ext.equals( GlobalThemeControl.ThemeSettings.EXTENSION.setting() ) ) { 
				String
					f_name = f_theme_names[ i ].getName( ).replaceFirst( "[.][^.]+$", ""); // Removing the extension.

				mntmMenuThemes[ i ] = new JMenuItem( f_name );
				mnThemes.add( mntmMenuThemes[ i ] );
					
				/*
				 * Here we are creating an event if the user decides to change themes here.
				 * Refer to GlobalThemeControl.Java for the loadTheme process.
				 */
				mntmMenuThemes[ i ].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						try {
							GlobalThemeControl.loadTheme( f_filename );
						} catch (IOException e1) {
							e1.printStackTrace();
						} finally {
							ClientSettingsControl.saveSettings();
						}
					}
				});	
			}
		}
		
		// Changing all GUI related values in relation to the user's settings.
		volumeSlider.setValue( MasterControlSound.i_volume_abs );
		
		chckbxmntmBountyRunes.setSelected( TimerControlEvents.b_bountyrunes ? true : false );
		chckbxmntmCampStacks.setSelected( TimerControlEvents.b_campstacks ? true : false );
		chckbxmntmSeconds.setSelected( TimerControlEvents.b_thirty ? true : false );
		chckbxmntmSeconds_1.setSelected( TimerControlEvents.b_fifteen ? true : false );
		chckbxmntmOnTime.setSelected( TimerControlEvents.b_onTime ? true : false );

		
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(null);
		lblBackground.setBounds(0, 0, 665, 371);
		contentPane.add(lblBackground);
		
		/*
		 * This is where GlobalThemeControl becomes extremely active. This is a bit sloppy 
		 * but I'm testing other alternatives at the moment. 
		 * 
		 * The timer repeats every second. It changes the GUI as well. This allows the clock
		 * to change its value.
		 */
		Timer 
			t_clock = new Timer();

		t_clock.scheduleAtFixedRate( new TimerTask( ) {
			@Override
			public void run( ) {
				
				/*
				 * Checks if a theme reset is scheduled to happen. I did this to avoid going through the
				 * complications of messing with lblBackground outside of this class.
				 */
				if( GlobalThemeControl.b_theme_reset ) {
					
					/*
					 * The program is going to try to load the image from the theme. JFrame was used to create a dialogue to let the 
					 * user know if the theme wasn't set. More information is found in NotificationDialogClient.java
					 */

					try {
						BufferedImage i_wall = ImageIO.read( new File( GlobalThemeControl.s_run_path + GlobalThemeControl.ThemeSettings.FOLDER.setting() + GlobalThemeControl.s_wallpaper_path ) );
						
						lblBackground.setIcon( new ImageIcon( i_wall ) );
						
						lblGameTime.setForeground( Color.decode( GlobalThemeControl.s_game_clock_code ) );
						lblTime.setForeground( Color.decode( GlobalThemeControl.s_clock_code ) );
						
						/*
						 * This will make sure the default theme was loaded before the GUI is visible.
						 */
						if( isLoadingTheme ) {
							isLoadingTheme = false;
							frame.setVisible(true);
						}
						else
							NotificationDialogClient.Show("Theme", "Your new theme has been set!");
						
					} catch (IOException e) {
						e.printStackTrace();
						
						/*
						 * If the theme couldn't be set during load time, the application will close itself and tell the user to check their default theme file.
						 * Otherwise everything should run without a problem.
						 */
						if( isLoadingTheme ) {
							NotificationDialogClient.Error("Theme Error", "There was an error loading your default theme. Check the 'themes' folder.");
							System.exit( 0 );
						}
						else
							NotificationDialogClient.Error("Theme Error", "This theme could not be set!");
					}
					
					/*
					 * Resetting b_theme_reset because we don't want your theme to constantly change just because you decided to change it
					 * in the first place.
					 */
					GlobalThemeControl.b_theme_reset = false;
				}
				
				/*
				 * Checking if the clock timer is active or not. If the timer isn't active, I don't want this code
				 * to continue to process.
				 */
				if( cwt.isTimerActive() ) {		
					cwt.updateTimerValues();
					
					/*
					 * This directly grabs the static values of the ClockwerkTimer class and sets
					 * the GUI's clock to the right value. It will format the time correctly.
					 */
					int 
						i_mili = TimerControlEvents.i_time,
						i_gt = TimerControlEvents.i_game;
					
					lblTime.setText( cwt.FormatTime( i_mili ) );
					lblGameTime.setText( cwt.FormatTime( i_gt ) );
					
					/*
					 * This makes the current game time show once the timer is active.
					 * I didn't want to have two different clocks show up on the default screen.
					 */
					if( !lblGameTime.isVisible(  ) ) {
						lblGameTime.setVisible( true );
						lblGameTime.setForeground( Color.decode( GlobalThemeControl.s_game_clock_code ) );
					}
				}
				
				/*
				 * This checks if the timer isn't active and the current main timer isn't on exactly
				 * two minutes.
				 * 
				 * The reason why the 2nd condition is in there is to make sure the game time doesn't
				 * just randomly pop up red just because the timer isn't active. It's only supposed to
				 * show up red once the user stops the timer AFTER a game was first started.
				 */
				else if( !cwt.isTimerActive() && TimerControlEvents.i_time != 120000 ) {
					
					/*
					 * Setting GUI back to normal.
					 */
					lblTime.setText("00:00");
					lblGameTime.setForeground(Color.RED);
					cwt.resetTimerValues( true );
				}
			}
		}, 1000, 1000);
	}
}
