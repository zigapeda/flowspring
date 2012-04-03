package de.zigapeda.flowspring.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.interfaces.TreeRow;


public class Interpret implements TreeRow {
	private int id;
	private String name;
	private Integer album;
	private String genre;
	private String comment;
	private Integer track;
	private String year;
	private Integer duration;
	private Integer rating;
	private Integer playcount;
	
	public Interpret(int id, String name, Integer album, String genre, String comment, Integer track, String year, Integer duration, Integer rating, Integer playcount) {
		this.id = id;
		this.name = name;
		this.album = album;
		this.genre = genre;
		this.comment = comment;
		this.track = track;
		this.year = year;
		this.duration = duration;
		this.rating = rating;
		this.playcount = playcount;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Integer getInt() {
		return null;
	}
	
	public String getName() {
		return this.name;
	}

	public String getArtist() {
		return this.name;
	}

	public String getAlbum() {
		if(this.album != null) {
			if(this.album > 1) {
				return this.album.toString() + " albums";
			} else {
				return this.album.toString() + " album";
			}
		}
		return null;
	}

	public String getGenre() {
		return this.genre;
	}

	public String getTrack() {
		return this.track.toString() + " tracks";
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
		if(rating >= 0 && rating <= 5) {
			return TreeRow.RATINGS[rating];
		}
		return TreeRow.RATINGS[0];
	}

	public String getPlaycount() {
		return this.playcount.toString();
	}

	public Integer getType() {
		return TreeRow.Interpret;
	}
	
	public static List<DataNode> getInterprets(String stmt, DataNode parent) {
        List<DataNode> nodes = new ArrayList<DataNode>();
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			if(stmt != null) {
				r = s.executeQuery("select stk_int_id, stk_interpret, count(distinct stk_alb_id), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), case when min(stk_year) = max(stk_year) then min(stk_year)||'' else min(stk_year)||' - '||max(stk_year) end, sum(stk_duration), avg(stk_rating), sum(stk_playcount)  from soundtracks where " + stmt + " group by stk_int_id, stk_interpret order by stk_interpret");
			} else {
				r = s.executeQuery("select stk_int_id, stk_interpret, count(distinct stk_alb_id), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), case when min(stk_year) = max(stk_year) then min(stk_year)||'' else min(stk_year)||' - '||max(stk_year) end, sum(stk_duration), avg(stk_rating), sum(stk_playcount)  from soundtracks group by stk_int_id, stk_interpret order by stk_interpret");
			}
			while(r.next()) {
				String gre = r.getString(4);
				if(gre.startsWith(", ")) {
					gre = gre.substring(2);
				}
				if(r.getString(2) != null) {
					nodes.add(new DataNode(new Interpret(r.getInt(1), r.getString(2), r.getInt(3), gre, r.getString(5), r.getInt(6), r.getString(7), r.getInt(8), r.getInt(9), r.getInt(10)),parent,null));
				} else {
					nodes.add(new DataNode(new Interpret(0, "<no interpret>", r.getInt(3), gre, r.getString(5), r.getInt(6), r.getString(7), r.getInt(8), r.getInt(9), r.getInt(10)),parent,null));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return nodes;
	}
}
