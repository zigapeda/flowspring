package de.zigapeda.flowspring.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.JViewport;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.data.PlaylistTrack;
import de.zigapeda.flowspring.data.Title;
import de.zigapeda.flowspring.data.YoutubeVideo;
import de.zigapeda.flowspring.gui.treetable.TreeTable;
import de.zigapeda.flowspring.gui.treetable.TreeTableCellRenderer;
import de.zigapeda.flowspring.interfaces.TreeRow;

public class MediaLibraryListener implements MouseListener, KeyListener, TreeExpansionListener {
	private TreeTable medialibrary;
	private Date lastpress;
	private String searchstring;
	private boolean control;
	
	public MediaLibraryListener(TreeTable medialibrary) {
		this.medialibrary = medialibrary;
		this.lastpress = new Date();
		this.searchstring = new String();
	}

	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			TreeRow selrow = this.medialibrary.getValueAt(this.medialibrary.getSelectedRow());
			if(selrow != null) {
				if(selrow.getType() == TreeRow.Title) {
					Main.getWindow().getPlaylist().addTrack(new PlaylistTrack(selrow.getId(), selrow.getArtist() + " - " + selrow.getName(),selrow.getInt(),((Title)selrow).getPath()));
				} else if(selrow.getType() == TreeRow.YoutubeVideo) {
					Main.getWindow().getPlaylist().addTrack(new PlaylistTrack(selrow.getName(),selrow.getInt(),((YoutubeVideo)selrow).getVideoUrl()));
				} else {
					int temp = this.medialibrary.getSelectedRow();
					TreeTableCellRenderer ttcr = ((TreeTableCellRenderer)this.medialibrary.getCellRenderer(this.medialibrary.getSelectedRow(), 0));
					if(ttcr.isCollapsed(this.medialibrary.getSelectedRow())) {
						ttcr.expandRow(this.medialibrary.getSelectedRow());
						this.medialibrary.getSelectionModel().setSelectionInterval(temp + 1, temp + 1);
						this.scrollTo(temp + this.medialibrary.getNodeAt(temp).getChildren().size());
						this.scrollTo(temp);
					} else {
						ttcr.collapseRow(this.medialibrary.getSelectedRow());
					}
				}
			}
		} else if(e.getButton() == MouseEvent.BUTTON3) {
			int temp = this.medialibrary.rowAtPoint(e.getPoint());
			this.medialibrary.getSelectionModel().setSelectionInterval(temp, temp);
			TreeRow selrow = this.medialibrary.getValueAt(temp);
			Main.getWindow().showMedialibraryMenu(selrow, e.getX(), e.getY());
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

	public void treeExpanded(TreeExpansionEvent event) {
		int temp = this.medialibrary.getTree().getRowForPath(event.getPath());
		this.scrollTo(temp + this.medialibrary.getNodeAt(temp).getChildren().size());
		this.scrollTo(temp);
	}

	public void treeCollapsed(TreeExpansionEvent event) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				if(this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() != 1) {
					int temp = this.medialibrary.getSelectedRow();
					((TreeTableCellRenderer)this.medialibrary.getCellRenderer(this.medialibrary.getSelectedRow(), 0)).expandRow(this.medialibrary.getSelectedRow());
					this.medialibrary.getSelectionModel().setSelectionInterval(temp + 1, temp + 1);
					this.scrollTo(temp + this.medialibrary.getNodeAt(temp).getChildren().size());
					this.scrollTo(temp);
				}
				e.consume();
				break;
			case KeyEvent.VK_LEFT:
				System.out.println(this.medialibrary.getNodeAt(this.medialibrary.getSelectedRow()).isRoot());
				if(this.medialibrary.getNodeAt(this.medialibrary.getSelectedRow()).isRoot() == true) {
					if(this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() != TreeRow.Title
							&& this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() != TreeRow.YoutubeVideo) {
						int temp = this.medialibrary.getSelectedRow();
						((TreeTableCellRenderer)this.medialibrary.getCellRenderer(this.medialibrary.getSelectedRow(), 0)).collapseRow(this.medialibrary.getSelectedRow());
						this.medialibrary.getSelectionModel().setSelectionInterval(temp, temp);
						this.scrollTo(temp);
					}
				} else {
					if(this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() != TreeRow.Title
							&& this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() != TreeRow.YoutubeVideo) {
						if(((TreeTableCellRenderer)this.medialibrary.getCellRenderer(this.medialibrary.getSelectedRow(), 0)).isExpanded(this.medialibrary.getSelectedRow())) {
							int temp = this.medialibrary.getSelectedRow();
							((TreeTableCellRenderer)this.medialibrary.getCellRenderer(this.medialibrary.getSelectedRow(), 0)).collapseRow(this.medialibrary.getSelectedRow());
							this.medialibrary.getSelectionModel().setSelectionInterval(temp, temp);
							this.scrollTo(temp);
						} else {
							int temp = this.medialibrary.getParentRowAt(this.medialibrary.getSelectedRow());
							((TreeTableCellRenderer)this.medialibrary.getCellRenderer(temp, 0)).collapseRow(temp);
							this.medialibrary.getSelectionModel().setSelectionInterval(temp, temp);
							this.scrollTo(temp);
						}
					} else {
						int temp = this.medialibrary.getParentRowAt(this.medialibrary.getSelectedRow());
						((TreeTableCellRenderer)this.medialibrary.getCellRenderer(temp, 0)).collapseRow(temp);
						this.medialibrary.getSelectionModel().setSelectionInterval(temp, temp);
						this.scrollTo(temp);
					}
				}
				e.consume();
				break;
			case KeyEvent.VK_CONTROL:
				this.control = true;
				break;
			case KeyEvent.VK_ENTER:
				if(this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() == TreeRow.Title) {
					int temp = this.medialibrary.getSelectedRow();
					TreeRow selrow = this.medialibrary.getValueAt(temp);
					Main.getWindow().getPlaylist().addTrack(new PlaylistTrack(selrow.getId(), selrow.getArtist() + " - " + selrow.getName(),selrow.getInt(),((Title)selrow).getPath()));
					this.medialibrary.getSelectionModel().setSelectionInterval(temp + 1, temp + 1);
				} else if(this.medialibrary.getValueAt(this.medialibrary.getSelectedRow()).getType() == TreeRow.YoutubeVideo) {
					int temp = this.medialibrary.getSelectedRow();
					TreeRow selrow = this.medialibrary.getValueAt(temp);
					Main.getWindow().getPlaylist().addTrack(new PlaylistTrack(selrow.getName(),selrow.getInt(),((YoutubeVideo)selrow).getVideoUrl()));
					this.medialibrary.getSelectionModel().setSelectionInterval(temp + 1, temp + 1);
				} else {
					int temp = this.medialibrary.getSelectedRow();
					((TreeTableCellRenderer)this.medialibrary.getCellRenderer(temp, 0)).expandRow(temp);
					this.medialibrary.getSelectionModel().setSelectionInterval(temp + 1, temp + 1);
				}
				e.consume();
				break;
		}
		if(e.getKeyChar() != 65535 && e.getKeyChar() != 10) {
			if(this.control) {
				if(e.getKeyCode() == 70) {
					Main.getWindow().setSearch("");
				}
			} else {
				if(this.lastpress.getTime() + 2000 < new Date().getTime()) {
					this.searchstring = "";
				}
				this.searchstring = this.searchstring + e.getKeyChar();
				this.lastpress = new Date();
				int currow = this.medialibrary.getSelectedRow();
				if(!this.medialibrary.getValueAt(currow).getName().toLowerCase().startsWith(this.searchstring)) {
					int newrow = currow + 1;
					if(this.medialibrary.getRowCount() == newrow) {
						newrow = 0;
					}
					while(this.medialibrary.getValueAt(newrow).getName().toLowerCase().startsWith(this.searchstring) == false && newrow != currow) {
						newrow++;
						if(this.medialibrary.getRowCount() == newrow) {
							newrow = 0;
						}
					}
					if(newrow == currow) {
						Main.getWindow().setSearch(this.searchstring);
						this.searchstring = "";
					} else {
						this.medialibrary.getSelectionModel().setSelectionInterval(newrow, newrow);
					}
					this.scrollTo(newrow);
				}
			}
		} else {
			this.searchstring = "";
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			this.control = false;
		}
	}
	
	private void scrollTo(int newrow) {
	    JViewport viewport = (JViewport)this.medialibrary.getParent();
	    Rectangle rect = this.medialibrary.getCellRect(newrow, 0, true);
	    Point pt = viewport.getViewPosition();
	    rect.setLocation(rect.x-pt.x, rect.y-pt.y);
	    viewport.scrollRectToVisible(rect);
	}

}
