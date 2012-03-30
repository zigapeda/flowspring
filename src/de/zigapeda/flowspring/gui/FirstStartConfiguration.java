package de.zigapeda.flowspring.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.zigapeda.flowspring.Main;

public class FirstStartConfiguration extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 4153028281541084094L;
	
	private JTextField directorytextfield;
	private JButton directorybrowse;
	private JButton savebutton;

	private JTextField directorystructure;

	private JTextField filestructure;

	public FirstStartConfiguration() {
		super("flowspring - initial configuration");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.setMinimumSize(new Dimension(600,400));
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screensize.width/2 - 300, screensize.height/2 - 200, 600, 400);
        BufferedImage image;
        try {
            image = ImageIO.read(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/icon.png"));
            this.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GridBagConstraints c = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		this.add(new JLabel("Default music directory: "),c);
		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		File f = new File(System.getProperty("user.home") + "/Music/");
		directorytextfield = new JTextField(f.getAbsolutePath());
		this.add(directorytextfield,c);
		c.weightx = 0;
		c.gridx = 2;
		c.fill = GridBagConstraints.NONE;
		directorybrowse = new JButton("Browse...");
		directorybrowse.addActionListener(this);
		this.add(directorybrowse,c);
		c.gridy = 1;
		c.gridx = 0;
		this.add(new JLabel("Directorystructure: "),c);
		c.gridx = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.directorystructure = new JTextField("<Interpret>/<Album>/");
		this.add(this.directorystructure,c);
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridy = 2;
		c.gridx = 0;
		this.add(new JLabel("Filename: "),c);
		c.gridx = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.filestructure = new JTextField("<Title> - <Interpret>");
		this.add(this.filestructure,c);
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridy++;
		c.gridwidth = 3;
		c.weightx = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.SOUTHEAST;
		savebutton = new JButton("Save");
		savebutton.addActionListener(this);
		this.add(savebutton,c);
		this.setVisible(true);
	}
	
	private String getDefaultDirectory() {
		File f = new File(this.directorytextfield.getText());
		if(f.isDirectory() == true) {
			return f.getAbsolutePath();
		} else if(f.mkdirs() == true) {
			return f.getAbsolutePath();
		}
		return "";
	}
	
	private String getDirectoryStructure() {
		String dirstructure = this.directorystructure.getText();
		return dirstructure;
	}
	
	private String getFileStructure() {
		String filestructure = this.filestructure.getText();
		return filestructure;
	}
	
	private void save() {
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			s.executeUpdate("merge into settings using (values('defaultdir','" + this.getDefaultDirectory() + "')) " +
					"as vals(x,y) on settings.set_name = vals.x " +
					"when matched then update set settings.set_value = vals.y " +
					"when not matched then insert values vals.x, vals.y ");
			s.executeUpdate("merge into settings using (values('dirstructure','" + this.getDirectoryStructure() + "')) " +
					"as vals(x,y) on settings.set_name = vals.x " +
					"when matched then update set settings.set_value = vals.y " +
					"when not matched then insert values vals.x, vals.y ");
			s.executeUpdate("merge into settings using (values('filestructure','" + this.getFileStructure() + "')) " +
					"as vals(x,y) on settings.set_name = vals.x " +
					"when matched then update set settings.set_value = vals.y " +
					"when not matched then insert values vals.x, vals.y ");
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.directorybrowse) {
			File file = new File(this.directorytextfield.getText());
			if(file.exists()) {
				if(file.isDirectory()) {
					JFileChooser fc = new JFileChooser(file);
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showOpenDialog(this.directorybrowse);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                this.directorytextfield.setText(fc.getSelectedFile().getAbsolutePath());
		            }
				} else {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = fc.showOpenDialog(this.directorybrowse);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                this.directorytextfield.setText(fc.getSelectedFile().getAbsolutePath());
		            }
				}
			} else {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(this.directorybrowse);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                this.directorytextfield.setText(fc.getSelectedFile().getAbsolutePath());
	            }
			}
		} else if(e.getSource() == this.savebutton) {
			save();
			this.dispose();
		}
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		save();
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
