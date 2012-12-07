package de.zigapeda.flowspring.data;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class PlaylistTrack implements Transferable {
	public static final DataFlavor DATAFLAVOR = new DataFlavor(PlaylistTrack.class, "PLAYLISTTRACK");
	
	private Integer id;
	private String name;
	private int duration;
	private String path;
	
	public PlaylistTrack(Integer id, String name, int duration, String path) {
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.path = path;
	}
	
	public PlaylistTrack(String name, int duration, String path) {
		this.name = name;
		this.duration = duration;
		this.path = path;
		this.id = Title.getTitleIdByPath(path);
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public int getDuration() {
		return this.duration;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String toString() {
		return this.name;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {DATAFLAVOR};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DATAFLAVOR);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return this;
	}
}
