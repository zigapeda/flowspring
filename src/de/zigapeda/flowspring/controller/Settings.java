package de.zigapeda.flowspring.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.gui.FirstStartConfiguration;

public class Settings {
	public static String loadSettings(String name) {
		Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			r = s.executeQuery("select set_value from settings where set_name  = '" + name + "'");
			while(r.next()) {
				return r.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public static void saveSettings(String key, String value) {
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			if(value == null) {
				s.executeUpdate("merge into settings using (values('" + key + "',null)) " +
						"as vals(x,y) on settings.set_name = vals.x " +
						"when matched then update set settings.set_value = vals.y " +
						"when not matched then insert values vals.x, vals.y ");
			} else {
				s.executeUpdate("merge into settings using (values('" + key + "','" + value + "')) " +
						"as vals(x,y) on settings.set_name = vals.x " +
						"when matched then update set settings.set_value = vals.y " +
						"when not matched then insert values vals.x, vals.y ");
			}
			s.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void firstStart() {
		int opt = JOptionPane.showConfirmDialog(null,
				"This is the first programm start. Do you want to setup base settings?", "First start detected",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(opt == JOptionPane.YES_OPTION) {
			Main.setOntopwindow(new FirstStartConfiguration());
		}
	}
}
