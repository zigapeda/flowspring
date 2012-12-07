package de.zigapeda.flowspring.gui.treetable;
 
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

import javax.swing.JTable;
import javax.swing.tree.TreePath;

import de.zigapeda.flowspring.controller.MediaLibraryListener;
import de.zigapeda.flowspring.data.DataNode;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.data.Title;
import de.zigapeda.flowspring.data.YoutubeVideo;
import de.zigapeda.flowspring.interfaces.TreeRow;
 
public class TreeTable extends JTable {
	private static final long	serialVersionUID	= -2433194227243028278L;
	private TreeTableCellRenderer tree;
	private MediaLibraryListener medialibrarylistener;
     
     
    public TreeTable(AbstractTreeTableModel treeTableModel) {
        super();
 
        // JTree erstellen.
        tree = new TreeTableCellRenderer(this, treeTableModel);
         
        // Modell setzen.
        super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
         
        // Gleichzeitiges Selektieren fuer Tree und Table.
        TreeTableSelectionModel selectionModel = new TreeTableSelectionModel();
        tree.setSelectionModel(selectionModel); //For the tree
        setSelectionModel(selectionModel.getListSelectionModel()); //For the table
 
        // Renderer fuer den Tree.
        setDefaultRenderer(TreeTableModel.class, tree);
        // Editor fuer die TreeTable
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor(tree, this));
         
        // Kein Grid anzeigen.
        setShowGrid(false);
 
        // Keine Abstaende.
        setIntercellSpacing(new Dimension(0, 0));
        
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, new DragGestureListener() {
			@Override
			public void dragGestureRecognized(DragGestureEvent dge) {
//				TreeRow selrow = this.medialibrary.getValueAt(this.medialibrary.getSelectedRow());
//				if(selrow != null) {
//					if(selrow.getType() == TreeRow.Title) {
//						Main.getWindow().getPlaylist().addTrack(new PlaylistTrack(selrow.getArtist() + " - " + selrow.getName(),selrow.getInt(),((Title)selrow).getPath()));
//					
				int row = TreeTable.this.getSelectedRow();
				if(row != -1) {
					TreeRow selrow = TreeTable.this.getValueAt(row);
					if(selrow.getType() == TreeRow.Title) {
						dge.startDrag(DragSource.DefaultCopyDrop, new PlaylistTrack(selrow.getId(), selrow.getArtist() + " - " + selrow.getName(),selrow.getInt(),((Title)selrow).getPath()));
					} else if(selrow.getType() == TreeRow.YoutubeVideo) {
						dge.startDrag(DragSource.DefaultCopyDrop, new PlaylistTrack(selrow.getName(),selrow.getInt(),((YoutubeVideo)selrow).getVideoUrl()));
					}
				}
			}
        });
    }
    
    public void setModel(AbstractTreeTableModel treeTableModel) {
    	tree = new TreeTableCellRenderer(this, treeTableModel);
    	if(this.medialibrarylistener != null) {
    		tree.addTreeExpansionListener(this.medialibrarylistener);
    	}
    	super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
    	TreeTableSelectionModel selectionModel = new TreeTableSelectionModel();
        tree.setSelectionModel(selectionModel);
        setSelectionModel(selectionModel.getListSelectionModel());
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor(tree, this));
    }
    
    public TreeTableCellRenderer getTree() {
    	return tree;
    }
    
    public TreePath getPathAt(int row) {
    	TreePath treePath = tree.getPathForRow(row);
    	return treePath;
    }
    
    public TreeRow getValueAt(int row) {
    	TreePath treePath = tree.getPathForRow(row);
    	TreeRow data = ((DataNode)treePath.getLastPathComponent()).getData();
    	return data;
    }
    
    public DataNode getNodeAt(int row) {
    	TreePath treePath = tree.getPathForRow(row);
    	return (DataNode)treePath.getLastPathComponent();
    }
    
    public int getParentRowAt(int row) {
    	TreePath treePath = tree.getPathForRow(row);
    	return tree.getRowForPath(treePath.getParentPath());
    }

	public void setListener(MediaLibraryListener medialibrarylistener) {
		this.medialibrarylistener = medialibrarylistener;
	}
}