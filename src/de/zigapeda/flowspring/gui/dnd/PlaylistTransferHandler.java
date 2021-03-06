package de.zigapeda.flowspring.gui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import de.zigapeda.flowspring.data.PlaylistTrack;

public class PlaylistTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1004955125717203527L;
	private JList<PlaylistTrack> list;
	
	public PlaylistTransferHandler(JList<PlaylistTrack> list) {
		this.list = list;
	}
	
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

    public boolean importData(TransferSupport support) {
    	try {
			PlaylistTrack plt = (PlaylistTrack) support.getTransferable().getTransferData(PlaylistTrack.DATAFLAVOR);
	    	DefaultListModel<PlaylistTrack> playlistmodel = (DefaultListModel<PlaylistTrack>) this.list.getModel();
	    	int insertAt = this.list.getDropLocation().getIndex();
	    	int indexInList = playlistmodel.indexOf(plt);
	    	if(indexInList != -1) {
		    	if(indexInList < insertAt) {
		    		insertAt--;
		    	}
		    	playlistmodel.removeElement(plt);
	    	}
	    	playlistmodel.insertElementAt(plt, insertAt);
	    	ListSelectionModel sm = this.list.getSelectionModel();
	    	sm.setSelectionInterval(insertAt, insertAt);
	    	return true;
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return false;
    }
    
    public boolean importData(JComponent comp, Transferable t) {
    	System.out.println(2);
        return false;
    }

    public boolean canImport(TransferSupport support) {
    	for(DataFlavor f: support.getDataFlavors()) {
    		if(f.equals(PlaylistTrack.DATAFLAVOR)) {
    			return true;
    		}
    	}
        return false;
    }
    
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
    	System.out.println(4);
        return false;
    }
    
    protected Transferable createTransferable(JComponent c) {
    	if(this.list.getSelectedIndex() != -1) {
    		return this.list.getSelectedValue();
    	}
    	return null;
    }
}
