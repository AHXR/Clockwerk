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
import com.sounds.DotaAudioController;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/*
 * This GUI was built through Eclipse's WindowBuilder (https://eclipse.org/windowbuilder/). 
 * There were a few adjustments made within the generated code that has been noted accordingly.
 */
public class ClockwerkGUI extends JFrame {
	
	private static final long serialVersionUID = -2400434487677545157L;
	private JPanel contentPane;

	// main()
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Prepares the GUI.
				try {
					ClockwerkGUI frame = new ClockwerkGUI();
					frame.setVisible(true);
				} catch (Exception e) {
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
	public ClockwerkGUI() {
		/*
		 * Here is where I create a new instance called "cwt" which stands for (C)lock(W)erk(T)imer.
		 * This instance was used to call functions within the class found inside "ClockwerkTimer.java".
		 * 
		 * Any sort of change to the user's settings directly called a function that would change
		 * how the timer would react. Simple stuff.
		 */
		ClockwerkTimer cwt = new ClockwerkTimer( );
		
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
		menuBar.setBounds(0, 0, 655, 21);
		contentPane.add(menuBar);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JCheckBoxMenuItem chckbxmntmBountyRunes = new JCheckBoxMenuItem("Bounty Runes");
		chckbxmntmBountyRunes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				cwt.setBountyRunesToggle( chckbxmntmBountyRunes.isSelected( ) );
			}
		});
		chckbxmntmBountyRunes.setSelected(true);
		mnSettings.add(chckbxmntmBountyRunes);
		
		JCheckBoxMenuItem chckbxmntmCampStacks = new JCheckBoxMenuItem("Camp Stacks");
		chckbxmntmCampStacks.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				cwt.setCampStacksToggle( chckbxmntmCampStacks.isSelected() );
			}
		});
		chckbxmntmCampStacks.setSelected(true);
		mnSettings.add(chckbxmntmCampStacks);
		
		JMenu mnWarnings = new JMenu("Warnings");
		mnSettings.add(mnWarnings);
		
		JCheckBoxMenuItem chckbxmntmSeconds = new JCheckBoxMenuItem("30 Seconds");
		chckbxmntmSeconds.setSelected(true);
		chckbxmntmSeconds.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				cwt.setWarningSignal(30, chckbxmntmSeconds.isSelected() );
			}
		});
		mnWarnings.add(chckbxmntmSeconds);
		
		JCheckBoxMenuItem chckbxmntmSeconds_1 = new JCheckBoxMenuItem("15 Seconds");
		chckbxmntmSeconds_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				cwt.setWarningSignal(15, chckbxmntmSeconds_1.isSelected() );
			}
		});
		chckbxmntmSeconds_1.setSelected(true);
		mnWarnings.add(chckbxmntmSeconds_1);
		
		JCheckBoxMenuItem chckbxmntmOnTime = new JCheckBoxMenuItem("On Time");
		chckbxmntmOnTime.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				cwt.setWarningSignal(0, chckbxmntmOnTime.isSelected() );
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
				DotaAudioController dac = new DotaAudioController();
				dac.changeVolume( volumeSlider.getValue() );
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
		
		JLabel lblGameTime = new JLabel("0:00");
		lblGameTime.setVisible(false);
		lblGameTime.setForeground(Color.WHITE);
		lblGameTime.setFont(new Font("Piston Pressure", lblGameTime.getFont().getStyle(), 20));
		lblGameTime.setBounds(10, 314, 447, 36);
		contentPane.add(lblGameTime);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(ClockwerkGUI.class.getResource("/com/images/background.png")));
		lblBackground.setBounds(0, 0, 655, 361);
		contentPane.add(lblBackground);
		/*
		 * GUI Creation Ends
		 */
		
		/*
		 * This is where the cwt instance becomes extremely active. This is a bit sloppy 
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
				 * Checking if the timer is active or not. If the timer isn't active, I don't want this code
				 * to continue to process.
				 */
				if( cwt.isTimerActive() ) {		
					cwt.updateTimerValues();
					
					/*
					 * This directly grabs the static values of the ClockwerkTimer class and sets
					 * the GUI's clock to the right value. It will format the time correctly.
					 */
					int 
						i_mili = ClockwerkTimer.i_time,
						i_gt = ClockwerkTimer.i_game;
					
					lblTime.setText( cwt.FormatTime( i_mili ) );
					lblGameTime.setText( cwt.FormatTime( i_gt ) );
					
					/*
					 * This makes the current game time show once the timer is active.
					 * I didn't want to have two different clocks show up on the default screen.
					 */
					if( !lblGameTime.isVisible(  ) ) {
						lblGameTime.setVisible( true );
						lblGameTime.setForeground(Color.WHITE);
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
				else if( !cwt.isTimerActive() && ClockwerkTimer.i_time != 120000 ) {
					
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
