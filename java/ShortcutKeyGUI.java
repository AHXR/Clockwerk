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
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import clockwerk.global.GlobalKeyShortcuts;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;

/*
 * This class was created by using WindowBuilder.
 */
public class ShortcutKeyGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	JLabel lblText;
	JCheckBox chckbxCtrl;
	JCheckBox chckbxAlt;
	JTextField txtKey;
	JCheckBox chckbxShift;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShortcutKeyGUI frame = new ShortcutKeyGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ShortcutKeyGUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ShortcutKeyGUI.class.getResource("/clockwerk/images/icon.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}
		});
		setResizable(false);
		setTitle("Clockwerk - Shortcut Key Changer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 212);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblText = new JLabel("");
		lblText.setVerticalAlignment(SwingConstants.TOP);
		lblText.setHorizontalAlignment(SwingConstants.LEFT);
		lblText.setBounds(10, 11, 422, 77);
		contentPane.add(lblText);
		
		chckbxShift = new JCheckBox("SHIFT");
		chckbxShift.setBounds(10, 89, 61, 23);
		contentPane.add(chckbxShift);
		
		chckbxCtrl = new JCheckBox("CTRL");
		chckbxCtrl.setBounds(87, 89, 61, 23);
		contentPane.add(chckbxCtrl);
		
		chckbxAlt = new JCheckBox("ALT");
		chckbxAlt.setBounds(164, 89, 61, 23);
		contentPane.add(chckbxAlt);
		
		txtKey = new JTextField();
		txtKey.setBounds(10, 112, 422, 20);
		contentPane.add(txtKey);
		txtKey.setColumns(10);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				/*
				 * This block of code used to be in ShortcutKeyListener. However I moved it after
				 * the 1.4. update allowed for CTRL+SHIFT+ALT combinations.
				 */
				String
					s_text = txtKey.getText( );
				
				if( s_text != null ) {
			    	switch( ShortcutKeyListener.getKeyChange( ) ) {
			    		case 0: {
			    			GlobalKeyShortcuts.TOGGLE.changeValue( s_text );
			    			break;
			    		}
			    		
			    		case 1: {
			    			GlobalKeyShortcuts.MUTE.changeValue( s_text );
			    			break;
			    		}
			    	}
			    	
			    	// Updates the array of combo keys.
			    	ShortcutKeyListener.updateComboBasedOnKey( chckbxShift.isSelected( ), chckbxCtrl.isSelected( ), chckbxAlt.isSelected( ) );
			    	ClientSettingsControl.saveSettings();
			    }
				
				// Close the dialog.
				setVisible( false );
				dispose();
			}
		});
		btnConfirm.setBounds(87, 141, 106, 35);
		contentPane.add(btnConfirm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible( false );
				dispose();
			}
		});
		btnCancel.setBounds(235, 141, 106, 35);
		contentPane.add(btnCancel);
	}
}
