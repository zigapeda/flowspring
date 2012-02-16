package de.zigapeda.flowspring.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.zigapeda.flowspring.data.Column;

public class MedialibraryRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6911856360056074741L;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if(component instanceof JLabel) {
			JLabel label = (JLabel) component;
			label.setHorizontalAlignment(LEFT);
//			getName();		0
//			getArtist();	1
//			getAlbum();		2
//			getGenre();		3
//			getTrack();		4
//			getYear();		5
//			getDuration();	6
//			getComment();	7
//			getRating();	8	
//			getPlaycount();	9
			switch(Column.getMedialibrarycolumns().get(column).getColumn().getModelIndex()) {
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					try {
						Integer.valueOf((String)value);
						label.setHorizontalAlignment(RIGHT);
					} catch (NumberFormatException e) {}
					break;
				case 5:
					try {
						Integer.valueOf((String)value);
						label.setHorizontalAlignment(RIGHT);
					} catch (NumberFormatException e) {}
					break;
				case 6:
					if(value != null) {
						int duration = (int) value;
						int hor = 0;
						int min = 0;
						int sec = 0;
						if(duration > 59) {
							if(duration > 3599) {
								hor = duration / 3600;
								min = (duration % 3600) / 60;
								sec = duration % 60;
							} else {
								min = duration / 60;
								sec = duration % 60;
							}
						} else {
							sec = duration;
						}
						if(hor == 0) {
							if(sec < 10) {
								label.setText(String.valueOf(min) + ":0" + String.valueOf(sec));
							} else {
								label.setText(String.valueOf(min) + ":" + String.valueOf(sec));
							}
						} else {
							if(min < 10) {
								if(sec < 10) {
									label.setText(String.valueOf(hor) + ":0" + String.valueOf(min) + ":0" + String.valueOf(sec));
								} else {
									label.setText(String.valueOf(hor) + ":0" + String.valueOf(min) + ":" + String.valueOf(sec));
								}
							} else {
								if(sec < 10) {
									label.setText(String.valueOf(hor) + ":" + String.valueOf(min) + ":0" + String.valueOf(sec));
								} else {
									label.setText(String.valueOf(hor) + ":" + String.valueOf(min) + ":" + String.valueOf(sec));
								}
							}
						}
					}
					label.setHorizontalAlignment(RIGHT);
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					try {
						Integer.valueOf((String)value);
						label.setHorizontalAlignment(RIGHT);
					} catch (NumberFormatException e) {}
					break;
			}
			return label;
		}
		return component;
	}

}
