package de.zigapeda.flowspring.data;

import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

public class ReadTableModel extends AbstractTableModel {
	private static final long	serialVersionUID	= 2580543196750928847L;
	
	private LinkedList<Title> data;
	
	public ReadTableModel() {
		data = new LinkedList<>();
//		DefaultTableModel
	}
	
	public void addRow(Title title) {
		data.add(title);
		fireTableRowsInserted(data.size()-1, data.size()-1);
	}

	public void removeAllRows() {
		int temp = data.size() - 1;
		data.clear();
		fireTableRowsDeleted(0, temp);
	}
	
	public int getRowCount() {
		return data.size();
	}
	
	public LinkedList<Title> getData() {
		return this.data;
	}

	public int getColumnCount() {
		return 4;
	}
	
    public String getColumnName(int column) {
    	switch(column) {
    		case 0:
    			return "Path";
    		case 1:
    			return "Interpret";
    		case 2:
    			return "Album";
    		case 3:
    			return "Title";
    	}
    	return null;
    }
    
	public boolean isCellEditable(int row, int column) {
		if(column == 0) {
			return false;
		}
		return true;
	}

	public Object getValueAt(int row, int column) {
		if(data.size() > row) {
	    	switch(column) {
	    		case 0:
	    			return data.get(row).getPath();
	    		case 1:
	    			return data.get(row).getArtist();
	    		case 2:
	    			return data.get(row).getAlbum();
	    		case 3:
	    			return data.get(row).getName();
	    	}
		}
		return null;
	}
	public void setValueAt(String value, int row, int column) {
		switch(column) {
			case 1:
//				data.get(row).setArtist(value);
			case 2:
//				data.get(row).setAlbum(value);
			case 3:
//				data.get(row).setName(value);
		}
		fireTableCellUpdated(row, column);
	}
}



//
//
//public ReadTableModel() {
//	data = new LinkedList<>();
//}
//
//public void addRow(Title title) {
//	data.add(title);
//	fireTableRowsInserted(data.size()-1, data.size()-1);
//}
//
//public void insertRow(int row, Title title) {
//	data.add(row, title);
//	fireTableRowsInserted(row, row);
//}
//
//public void removeRow(int row) {
//	data.remove(row);
//}
//
//public boolean isCellEditable(int row, int column) {
//	if(column == 0) {
//		return false;
//	}
//	return true;
//}
//
//public Object getValueAt(int row, int column) {
//	if(data.size() > row) {
//    	switch(column) {
//    		case 0:
//    			return data.get(row).getPath();
//    		case 1:
//    			return data.get(row).getArtist();
//    		case 2:
//    			return data.get(row).getAlbum();
//    		case 3:
//    			return data.get(row).getName();
//    	}
//	}
//	return null;
//}
//
//public void setValueAt(String value, int row, int column) {
////	switch(column) {
////		case 1:
////			data.get(row).setArtist(value);
////		case 2:
////			data.get(row).setAlbum(value);
////		case 3:
////			data.get(row).setName(value);
////	}
//    fireTableCellUpdated(row, column);
//}