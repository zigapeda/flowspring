package de.zigapeda.flowspring.gui.treetable;
 
import de.zigapeda.flowspring.data.DataNode;
import de.zigapeda.flowspring.interfaces.TreeRow;
 
public class DataModel extends AbstractTreeTableModel {
    // Spalten Name.
    static public String[] columnNames = { "Name", "Artist", "Album", "Genre", "Track", "Year", "Duration", "Comment", "Rating", "Playcount" };
 
    // Spalten Typen.
    static protected Class<?>[] columnTypes = { TreeTableModel.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class };
 
    public DataModel(DataNode rootNode) {
        super(rootNode);
        root = rootNode;
    }
 
    public Object getChild(Object parent, int index) {
        return ((DataNode) parent).getChildren().get(index);
    }
 
 
    public int getChildCount(Object parent) {
    	Integer type = ((DataNode)parent).getData().getType();
    	if(type != TreeRow.Title  && type != TreeRow.YoutubeVideo) {
    		return ((DataNode) parent).getChildren().size();
    	}
    	return 0;
    }
 
 
    public int getColumnCount() {
        return columnNames.length;
    }
 
 
    public String getColumnName(int column) {
        return columnNames[column];
    }
 
 
    public Class<?> getColumnClass(int column) {
        return columnTypes[column];
    }
 
    public Object getValueAt(Object node, int column) {
    	TreeRow data = ((DataNode)node).getData();
    	switch(column) {
    		case 0:
    			return data.getName();
    		case 1:
    			return data.getArtist();
    		case 2:
    			return data.getAlbum();
    		case 3:
    			return data.getGenre();
    		case 4:
    			return data.getTrack();
    		case 5:
    			return data.getYear();
    		case 6:
    			return data.getDuration();
    		case 7:
    			return data.getComment();
    		case 8:
    			return data.getRating();
    		case 9:
    			return data.getPlaycount();
    	}
        return null;
    }
 
    public boolean isCellEditable(Object node, int column) {
    	if(column == 0) {
    		return true;
    	}
    	return false;
    }
 
    public void setValueAt(Object aValue, Object node, int column) {
    }
 
}