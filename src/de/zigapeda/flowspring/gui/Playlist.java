package de.zigapeda.flowspring.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import sun.swing.DefaultLookup;
import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.data.Title;

public class Playlist extends JPanel implements ListCellRenderer<PlaylistTrack>, MouseListener, KeyListener {
	private static final long	serialVersionUID	= -3606283321945550386L;
	
	private DefaultListModel<PlaylistTrack> playlistmodel;
	private PlaylistTrack track;
	private JList<PlaylistTrack> playlist;
	
	public Playlist() {
		super();
		this.track = null;
	    this.playlistmodel = new DefaultListModel<>();
	    this.playlist = new JList<>();
	    this.playlist.addKeyListener(this);
	    this.playlist.setModel(this.playlistmodel);
		this.playlist.setDragEnabled(true);
		this.playlist.setDropMode(DropMode.INSERT);
		this.playlist.setTransferHandler(new ListMoveTransferHandler());
		this.playlist.setCellRenderer(this);
		this.playlist.addMouseListener(this);
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(this.playlist));
		this.add(new PlaylistControlls(this.playlistmodel),BorderLayout.PAGE_END);
//		this.setBackground(new Color(115, 164, 209));
//		this.setSelectionBackground(new Color(115, 164, 209));
//		this.setSelectionForeground(new Color(0, 0, 0));
	}

	public PlaylistTrack getCurrent() {
		if(this.track == null) {
			this.track = this.getNext();
		}
		this.count();
		return this.track;
	}

	public PlaylistTrack getPrevious() {
		if(this.track == null) {
			if(this.playlistmodel.getSize() > 0) {
				this.track = this.playlistmodel.lastElement();
			} else {
				this.track = null;
			}
		} else {
			if(this.playlistmodel.indexOf(this.track) - 1 >= 0){
				this.track = this.playlistmodel.getElementAt(this.playlistmodel.indexOf(this.track) - 1);
			} else {
				this.track = null;
			}
		}
		this.repaint();
		this.count();
		return this.track;
	}

	public PlaylistTrack getNext() {
		if(this.track == null) {
			if(this.playlistmodel.getSize() > 0) {
				this.track = this.playlistmodel.firstElement();
			} else {
				this.track = null;
			}
		} else {
			if(this.playlistmodel.getSize() > this.playlistmodel.indexOf(this.track) + 1){
				this.track = this.playlistmodel.getElementAt(this.playlistmodel.indexOf(this.track) + 1);
			} else {
				this.track = null;
			}
		}
		this.repaint();
		this.count();
		return this.track;
	}
	
	private void count() {
		if(this.track != null) {
			if(this.track.getId() != null) {
				Title.addCount(this.track.getId());
			}
		}
	}

	public void addTrack(PlaylistTrack playlisttrack) {
		this.playlistmodel.addElement(playlisttrack);
		if(Main.getWindow().getPlayercontroller().isStopped()) {
			this.track = playlisttrack;
			Main.getWindow().getPlayercontroller().play();
		}
	}

	public Component getListCellRendererComponent(JList<? extends PlaylistTrack> list, PlaylistTrack value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label;
		if(value == this.track) {
			label = new JLabel("► " + value.toString());
		} else {
			label = new JLabel(value.toString());
		}
        if (cellHasFocus == true || value == this.track) {
        	Border border = null;
            if (isSelected == true) {
                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
            }
            label.setBorder(border);
        }
		if(isSelected == true) {
			label.setOpaque(true);
			label.setBackground(this.playlist.getSelectionBackground());
			label.setForeground(this.playlist.getSelectionForeground());
		}
        return label;
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			if(this.playlist.getSelectedIndex() != -1) {
				if(this.track != this.playlist.getSelectedValue()) {
					Main.getWindow().getPlayercontroller().stop();
					this.track = this.playlist.getSelectedValue();
					Main.getWindow().getPlayercontroller().play();
					this.repaint();
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			if(this.playlist.getSelectedIndex() != -1) {
				if(this.playlist.getSelectedValue() == this.track) {
					Main.getWindow().getPlayercontroller().stop();
				}
				int sel = this.playlist.getSelectedIndex();
				this.playlistmodel.remove(this.playlist.getSelectedIndex());
				if(this.playlistmodel.getSize() != 0) {
					if(this.playlistmodel.getSize() > sel) {
						this.playlist.setSelectedIndex(sel);
					} else {
						this.playlist.setSelectedIndex(sel - 1);
					}
				}
			}
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(this.playlist.getSelectedIndex() != -1) {
				if(this.track != this.playlist.getSelectedValue()) {
					Main.getWindow().getPlayercontroller().stop();
					this.track = this.playlist.getSelectedValue();
					Main.getWindow().getPlayercontroller().play();
					this.repaint();
				}
			}
		}
	}
	
	static class ListMoveDataFlavor extends DataFlavor {
		
		private final DefaultListModel<PlaylistTrack> model;
		
		public ListMoveDataFlavor(DefaultListModel<PlaylistTrack> model) {
			super(ListMoveTransferData.class, "List Data");
			this.model = model;
		}
		
		public DefaultListModel<PlaylistTrack> getModel() {
			return model;
		}
		
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((model == null) ? 0 : model.hashCode());
			return result;
		}
		
		public boolean equals(DataFlavor that) {
			if (this == that) {
				return true;
			}
			if (!super.equals(that) || getClass() != that.getClass()) {
				return false;
			}
			return match(model, that);
		}

		public static boolean match(DefaultListModel<PlaylistTrack> model, DataFlavor flavor) {
			return flavor instanceof ListMoveDataFlavor
					&& ((ListMoveDataFlavor) flavor).getModel() == model;
		}
	}
	
	private static class ListMoveTransferData {
		
		private final DefaultListModel<PlaylistTrack> model;
		private final int[] indices;
		
		ListMoveTransferData(DefaultListModel<PlaylistTrack> model, int[] indices) {
			this.model = model;
			this.indices = indices;
		}
		
		int[] getIndices() {
			return indices;
		}
		
		public DefaultListModel<PlaylistTrack> getModel() {
			return model;
		}
	}
	
	static class ListMoveTransferable implements Transferable {
		
		private final ListMoveTransferData	data;
		
		public ListMoveTransferable(ListMoveTransferData data) {
			this.data = data;
		}
		
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { new ListMoveDataFlavor(data.getModel()) };
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return ListMoveDataFlavor.match(data.getModel(), flavor);
		}
		
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return data;
		}
	}
	
	static class ListMoveTransferHandler extends TransferHandler {
		private static final long	serialVersionUID	= 6703461043403098490L;
		
		@SuppressWarnings("unchecked")
		public int getSourceActions(JComponent c) {
			final JList<PlaylistTrack> list = (JList<PlaylistTrack>) c;
			return list.getModel() instanceof DefaultListModel ? MOVE : NONE;
		}
		
		@SuppressWarnings("unchecked")
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			if (!(comp instanceof JList)
					|| !(((JList<PlaylistTrack>) comp).getModel() instanceof DefaultListModel)) {
				return false;
			}
			
			final DefaultListModel<PlaylistTrack> model = (DefaultListModel<PlaylistTrack>) ((JList<PlaylistTrack>) comp)
					.getModel();
			for (DataFlavor f : transferFlavors) {
				if (ListMoveDataFlavor.match(model, f)) {
					return true;
				}
			}
			return false;
		}
		
		@SuppressWarnings("unchecked")
		protected Transferable createTransferable(JComponent c) {
			final JList<PlaylistTrack> list = (JList<PlaylistTrack>) c;
			final int[] selectedIndices = list.getSelectedIndices();
			return new ListMoveTransferable(new ListMoveTransferData(
					(DefaultListModel<PlaylistTrack>) list.getModel(), selectedIndices));
		}
		
		@SuppressWarnings("unchecked")
		public boolean importData(TransferHandler.TransferSupport info) {
			final Component comp = info.getComponent();
			if (!info.isDrop() || !(comp instanceof JList)) {
				return false;
			}
			final JList<PlaylistTrack> list = (JList<PlaylistTrack>) comp;
			final ListModel<PlaylistTrack> lm = list.getModel();
			if (!(lm instanceof DefaultListModel)) {
				return false;
			}
			
			final DefaultListModel<PlaylistTrack> listModel = (DefaultListModel<PlaylistTrack>) lm;
			final DataFlavor flavor = new ListMoveDataFlavor(listModel);
			if (!info.isDataFlavorSupported(flavor)) {
				return false;
			}
			
			final Transferable transferable = info.getTransferable();
			try {
				final ListMoveTransferData data = (ListMoveTransferData) transferable
						.getTransferData(flavor);
				
				// get the initial insertion index
				final JList.DropLocation dropLocation = list.getDropLocation();
				int insertAt = dropLocation.getIndex();

				// get the indices sorted (we use them in reverse order, below)
				final int[] indices = data.getIndices();
				Arrays.sort(indices);
				
				// remove old elements from model, store them on stack
				final Stack<PlaylistTrack> elements = new Stack<PlaylistTrack>();
				int shift = 0;
				for (int i = indices.length - 1; i >= 0; i--) {
					final int index = indices[i];
					if (index < insertAt) {
						shift--;
					}
					elements.push(listModel.remove(index));
				}
				insertAt += shift;
				
				// insert stored elements from stack to model
				final ListSelectionModel sm = list.getSelectionModel();
				try {
					sm.setValueIsAdjusting(true);
					sm.clearSelection();
					final int anchor = insertAt;
					while (!elements.isEmpty()) {
						listModel.insertElementAt(elements.pop(), insertAt);
						sm.addSelectionInterval(insertAt, insertAt++);
					}
					final int lead = insertAt - 1;
					if (!sm.isSelectionEmpty()) {
						sm.setAnchorSelectionIndex(anchor);
						sm.setLeadSelectionIndex(lead);
					}
				} finally {
					sm.setValueIsAdjusting(false);
				}
				return true;
			} catch (UnsupportedFlavorException ex) {
				return false;
			} catch (IOException ex) {
				return false;
			}
		}
	}
}