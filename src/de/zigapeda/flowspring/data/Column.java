package de.zigapeda.flowspring.data;

import java.util.LinkedList;
import java.util.Vector;

import javax.swing.table.TableColumn;

import de.zigapeda.flowspring.gui.treetable.DataModel;

public class Column {
	private static LinkedList<Column> medialibrary;
	private TableColumn column;
	private int width;
	private boolean visible;
	
	public Column(TableColumn column, int width, boolean visible) {
		this.column = column;
		this.setWidth(width);
		this.setVisible(visible);
	}
	
	public TableColumn getColumn() {
		return this.column;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		if(visible == false) {
			this.width = 60;
		}
		this.visible = visible;
	}
	
	public static LinkedList<Column> getMedialibrarycolumns() {
		return Column.medialibrary;
	}
	
	public static void setMedialibrarycolumns(LinkedList<Column> columns) {
		Column.medialibrary = columns;
	}
	
	public static Column getColumnById(LinkedList<Column> list, TableColumn column) {
		for(Column c: list) {
			if(column == c.getColumn()) {
				return c;
			}
		}
		return null;
	}
	
	public static Column getColumnByName(LinkedList<Column> list, String name) {
		for(Column c: list) {
			if(name.equals(DataModel.columnNames[c.getColumn().getModelIndex()])){
				return c;
			}
		}
		return null;
	}

	public static Vector<Column> getHiddenColumns(LinkedList<Column> list) {
		Vector<Column> columns = new Vector<>();
		if(list != null) {
			for(Column c: list) {
				if(c.isVisible() == false) {
					columns.add(c);
				}
			}
		}
		return columns;
	}
	
	public static String getColumnlistString(LinkedList<Column> list) {
		String temp = new String();
		for(Column c: list) {
			if(c.isVisible() == true) {
				temp = temp + "t";
			} else {
				temp = temp + "f";
			}
			if(c.getWidth() < 1000) {
				if(c.getWidth() < 100) {
					if(c.getWidth() < 10) {
						temp = temp + "000" + String.valueOf(c.getWidth());
					} else {
						temp = temp + "00" + String.valueOf(c.getWidth());
					}
				} else {
					temp = temp + "0" + String.valueOf(c.getWidth());
				}
			} else {
				temp = temp + String.valueOf(c.getWidth());
			}
			temp = temp + DataModel.columnNames[c.getColumn().getModelIndex()] + ",";
		}
		return temp.substring(0, temp.length()-1);
	}
}
