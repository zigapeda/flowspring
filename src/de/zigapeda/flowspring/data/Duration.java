package de.zigapeda.flowspring.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.interfaces.TreeRow;

public class Duration implements TreeRow {
	private Integer name;
	private Integer artist;
	private Integer album;
	private String genre;
	private String comment;
	private Integer track;
	private String year;
	private Integer rating;
	private Integer playcount;
	
	public int getId() {
		return 0;
	}
	
	public Integer getInt() {
		return this.name;
	}
	
	public Duration (Integer name, Integer artist, Integer album, String genre, String comment, Integer track, String year, Integer rating, Integer playcount) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.genre = genre;
		this.comment = comment;
		this.track = track;
		this.year = year;
		this.rating = rating;
		this.playcount = playcount;
	}
	
	public String getName() {
		if(this.name != null) {
			int hor = 0;
			int min = 0;
			int sec = 0;
			if(this.name > 59) {
				if(this.name > 3599) {
					hor = this.name / 3600;
					min = (this.name % 3600) / 60;
					sec = this.name % 60;
				} else {
					min = this.name / 60;
					sec = this.name % 60;
				}
			} else {
				sec = this.name;
			}
			if(hor == 0) {
				if(sec < 10) {
					return String.valueOf(min) + ":0" + String.valueOf(sec);
				} else {
					return String.valueOf(min) + ":" + String.valueOf(sec);
				}
			} else {
				if(min < 10) {
					if(sec < 10) {
						return String.valueOf(hor) + ":0" + String.valueOf(min) + ":0" + String.valueOf(sec);
					} else {
						return String.valueOf(hor) + ":0" + String.valueOf(min) + ":" + String.valueOf(sec);
					}
				} else {
					if(sec < 10) {
						return String.valueOf(hor) + ":" + String.valueOf(min) + ":0" + String.valueOf(sec);
					} else {
						return String.valueOf(hor) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);
					}
				}
			}
		}
		return null;
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
		return this.name;
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
		return String.valueOf(this.playcount);
	}
	
	public int getDurationInt() {
		return this.name;
	}

	public Integer getType() {
		return TreeRow.Duration;
	}

	public static List<DataNode> getDurations(String stmt, DataNode parent) {
        List<DataNode> nodes = new ArrayList<DataNode>();
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			if(stmt != null) {
				r = s.executeQuery("select stk_duration, count(distinct stk_int_id), count(distinct stk_alb_id), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), min(stk_year) || ' - ' || max(stk_year), avg(stk_rating), sum(stk_playcount) from soundtracks where " + stmt + " group by stk_duration order by stk_duration");
			} else {
				r = s.executeQuery("select stk_duration, count(distinct stk_int_id), count(distinct stk_alb_id), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), min(stk_year) || ' - ' || max(stk_year), avg(stk_rating), sum(stk_playcount) from soundtracks group by stk_duration order by stk_duration");
			}
			while(r.next()) {
				String gre = r.getString(4);
				if(gre.startsWith(", ")) {
					gre = gre.substring(2);
				}
				nodes.add(new DataNode(new Duration(r.getInt(1), r.getInt(2), r.getInt(3), gre, r.getString(5), r.getInt(6), r.getString(7), r.getInt(8), r.getInt(9)),parent,null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return nodes;
	}
}