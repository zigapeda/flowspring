package de.zigapeda.flowspring.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.zigapeda.flowspring.Main;

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
}
