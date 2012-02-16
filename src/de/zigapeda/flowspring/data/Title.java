package de.zigapeda.flowspring.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.interfaces.TreeRow;

public class Title implements TreeRow {
	private int id;
	private String name;
	private String artist;
	private String album;
	private String genre;
	private String comment;
	private Integer track;
	private Integer year;
	private Integer duration;
	private Integer rating;
	private Integer playcount;
	private String path;
	
	public Title(int id, String name, String artist, String album, String genre, String comment, Integer track, Integer year, Integer duration, Integer rating, Integer playcount, String path) {
		this.id = id;
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.comment = comment;
		this.track = track;
		this.year = year;
		this.duration = duration;
		this.rating = rating;
		this.playcount = playcount;
		this.path = path;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Integer getInt() {
		return this.duration;
	}

	public String getName() {
		return this.name;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getAlbum() {
		return this.album;
	}

	public String getGenre() {
		return this.genre;
	}

	public String getTrack() {
		return this.track.toString();
	}

	public String getYear() {
		return this.year.toString();
	}

	public Integer getDuration() {
		return this.duration;
//		if(this.duration != null) {
//			int hor = 0;
//			int min = 0;
//			int sec = 0;
//			if(this.duration > 59) {
//				if(this.duration > 3599) {
//					hor = this.duration / 3600;
//					min = (this.duration % 3600) / 60;
//					sec = this.duration % 60;
//				} else {
//					min = this.duration / 60;
//					sec = this.duration % 60;
//				}
//			} else {
//				sec = this.duration;
//			}
//			if(hor == 0) {
//				if(sec < 10) {
//					return String.valueOf(min) + ":0" + String.valueOf(sec);
//				} else {
//					return String.valueOf(min) + ":" + String.valueOf(sec);
//				}
//			} else {
//				if(min < 10) {
//					if(sec < 10) {
//						return String.valueOf(hor) + ":0" + String.valueOf(min) + ":0" + String.valueOf(sec);
//					} else {
//						return String.valueOf(hor) + ":0" + String.valueOf(min) + ":" + String.valueOf(sec);
//					}
//				} else {
//					if(sec < 10) {
//						return String.valueOf(hor) + ":" + String.valueOf(min) + ":0" + String.valueOf(sec);
//					} else {
//						return String.valueOf(hor) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);
//					}
//				}
//			}
//		}
//		return null;
	}

	public String getComment() {
		return this.comment;
	}

	public String getRating() {
		if(this.rating >= 0 && this.rating <= 5) {
			return TreeRow.RATINGS[this.rating];
		}
		return TreeRow.RATINGS[0];
	}

	public String getPlaycount() {
		return this.playcount.toString();
	}
	
	public String getPath() {
		return this.path;
	}

	public Integer getType() {
		return TreeRow.Title;
	}

	public static List<DataNode> getTitles(String stmt, DataNode parent) {
        List<DataNode> nodes = new ArrayList<DataNode>();
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			if(stmt != null) {
				r = s.executeQuery("select distinct stk_ttl_id, stk_title, stk_interpret, stk_album, stk_genre, stk_comment, stk_track, stk_year, stk_duration, stk_rating, stk_playcount, stk_path from soundtracks where " + stmt + " order by stk_title");
			} else {
				r = s.executeQuery("select distinct stk_ttl_id, stk_title, stk_interpret, stk_album, stk_genre, stk_comment, stk_track, stk_year, stk_duration, stk_rating, stk_playcount, stk_path from soundtracks order by stk_title");
			}
			while(r.next()) {
				nodes.add(new DataNode(new Title(r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getString(6),r.getInt(7),r.getInt(8),r.getInt(9),r.getInt(10),r.getInt(11),r.getString(12)),parent,null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return nodes;
	}

	public static Integer getTitleIdByPath(String path) {
        Connection c = Main.getDatabase();
        try {
			PreparedStatement s = c.prepareStatement("select ttl_id from titles where ttl_path = ? ");
			s.setString(1, path);
			ResultSet r = s.executeQuery();
			if(r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return null;
	}

	public static List<DataNode> getTitlesBySearchstring(String searchstring) {
        List<DataNode> nodes = new ArrayList<DataNode>();
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			r = s.executeQuery("select distinct stk_ttl_id, stk_title, stk_interpret, stk_album, stk_genre, stk_comment, stk_track, stk_year, stk_duration, stk_rating, stk_playcount, stk_path from soundtracks where lower(stk_interpret) like '%" + searchstring + "%' or lower(stk_album) like '%" + searchstring + "%' or lower(stk_title) like '%" + searchstring + "%' order by stk_title");
			while(r.next()) {
				nodes.add(new DataNode(new Title(r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getString(6),r.getInt(7),r.getInt(8),r.getInt(9),r.getInt(10),r.getInt(11),r.getString(12)),null,null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return nodes;
	}
	
	public static void addCount(int id) {
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			s.executeUpdate("update titles set ttl_playcount = ttl_playcount + 1 where ttl_id = " + String.valueOf(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
