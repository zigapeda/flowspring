package de.zigapeda.flowspring.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.interfaces.TreeRow;

public class Playcount implements TreeRow {
	private Integer name;
	private Integer artist;
	private Integer album;
	private String genre;
	private String comment;
	private Integer track;
	private String year;
	private Integer duration;
	private Integer rating;
	
	public int getId() {
		return 0;
	}
	
	public Integer getInt() {
		return this.name;
	}
	
	public Playcount (Integer name, Integer artist, Integer album, String genre, String comment, Integer track, String year, Integer duration, Integer rating) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.comment = comment;
		this.track = track;
		this.year = year;
		this.duration = duration;
		this.rating = rating;
	}
	
	public String getName() {
		return this.name.toString();
	}

	public String getArtist() {
		if(this.artist == 1) {
			return String.valueOf(this.artist) + " interpret";
		}
		return String.valueOf(this.artist) + " interprets";
	}

	public String getAlbum() {
		if(this.album == 1) {
			return String.valueOf(this.album) + " album";
		}
		return String.valueOf(this.album) + " albums";
	}

	public String getGenre() {
		return this.genre;
	}

	public String getTrack() {
		return String.valueOf(this.track) + " tracks";
	}

	public String getYear() {
		return this.year;
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
		return this.name.toString();
	}

	public Integer getType() {
		return TreeRow.Playcount;
	}

	public static List<DataNode> getPlaycounts(String stmt, DataNode parent) {
        List<DataNode> nodes = new ArrayList<DataNode>();
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			if(stmt != null) {
				r = s.executeQuery("select stk_playcount, count(distinct stk_int_id), count(distinct stk_alb_id), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), min(stk_year) || ' - ' || max(stk_year), sum(stk_duration), avg(stk_rating) from soundtracks where " + stmt + " group by stk_playcount order by stk_playcount");
			} else {
				r = s.executeQuery("select stk_playcount, count(distinct stk_int_id), count(distinct stk_alb_id), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), min(stk_year) || ' - ' || max(stk_year), sum(stk_duration), avg(stk_rating) from soundtracks group by stk_playcount order by stk_playcount");
			}
			while(r.next()) {
				String gre = r.getString(4);
				if(gre.startsWith(", ")) {
					gre = gre.substring(2);
				}
				nodes.add(new DataNode(new Playcount(r.getInt(1), r.getInt(2), r.getInt(3), gre, r.getString(5), r.getInt(6), r.getString(7), r.getInt(8), r.getInt(9)),parent,null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return nodes;
	}
}