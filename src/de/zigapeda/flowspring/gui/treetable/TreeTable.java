package de.zigapeda.flowspring.gui.treetable;
 
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.tree.TreePath;

import de.zigapeda.flowspring.controller.MediaLibraryListener;
import de.zigapeda.flowspring.data.DataNode;
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