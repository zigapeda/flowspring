package de.zigapeda.flowspring.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ReadtableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = -1606593392594912659L;
	private ReadWindow readwindow;
	
	public ReadtableRenderer(ReadWindow readwindow) {
		this.readwindow = readwindow;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(component instanceof JLabel) {
			JLabel label = (JLabel) component;
			String string = (String) value;
			string = string.replace(this.readwindow.getPath(), "");
			label.setText("." + string);
			return label;
		}
		return component;
	}
}
