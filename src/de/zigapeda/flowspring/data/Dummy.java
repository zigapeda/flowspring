package de.zigapeda.flowspring.data;

import de.zigapeda.flowspring.interfaces.TreeRow;

public class Dummy implements TreeRow {
	
	public int getId() {
		return 0;
	}
	
	public Integer getInt() {
		return null;
	}
	
	public String getName() {
		return "dummy";
	}

	public String getArtist() {
		return null;
	}

	public String getAlbum() {
		return null;
	}

	public String getGenre() {
		return null;
	}

	public String getTrack() {
		return null;
	}

	public String getYear() {
		return null;
	}

	public Integer getDuration() {
		return null;
	}

	public String getComment() {
		return null;
	}

	public String getRating() {
		return null;
	}

	public String getPlaycount() {
		return null;
	}

	public Integer getType() {
		return TreeRow.Dummy;
	}
}