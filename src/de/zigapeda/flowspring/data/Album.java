package de.zigapeda.flowspring.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.interfaces.TreeRow;

public class Album implements TreeRow {
	private int id;
	private String name;
	private String interpret;
	private String genre;
	private String comment;
	private Integer track;
	private String year;
	private Integer duration;
	private Integer rating;
	private Integer playcount;

	public Album(int id, String name, String interpret, String genre, String comment, Integer track, String year, Integer duration, Integer rating, Integer playcount) {
		this.id = id;
		this.name = name;
		this.interpret = interpret;
		this.genre = genre;
		this.comment = comment;
		this.track = track;
		this.year = year;
		this.duration = duration;
		this.rating = rating;
		this.playcount = playcount;
	}
	
	public Album(int id, String name, Integer interpret, String genre, String comment, Integer track, String year, Integer duration, Integer rating, Integer playcount) {
		this.id = id;
		this.name = name;
		this.interpret = interpret.toString() + " interprets";
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
		return this.interpret;
	}

	public String getAlbum() {
		return this.name;
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
		if(this.rating >= 0 && this.rating <= 5) {
			return TreeRow.RATINGS[this.rating];
		}
		return TreeRow.RATINGS[0];
	}

	public String getPlaycount() {
		return this.playcount.toString();
	}

	public Integer getType() {
		return TreeRow.Album;
	}

	public static List<DataNode> getAlbums(String stmt, DataNode parent) {
        List<DataNode> nodes = new ArrayList<DataNode>();
        Connection c = Main.getDatabase();
        try {
			Statement s = c.createStatement();
			ResultSet r = null;
			if(stmt != null) {
				r = s.executeQuery("select stk_alb_id, stk_album, count(distinct stk_int_id), min(stk_interpret), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), min(stk_year)||' - '||max(stk_year), sum(stk_duration), avg(stk_rating), sum(stk_playcount) from soundtracks where " + stmt + " group by stk_alb_id, stk_album order by stk_album");
			} else {
				r = s.executeQuery("select stk_alb_id, stk_album, count(distinct stk_int_id), min(stk_interpret), group_concat(distinct stk_genre order by stk_genre separator ', '), min(stk_comment), count(stk_track), min(stk_year)||' - '||max(stk_year), sum(stk_duration), avg(stk_rating), sum(stk_playcount) from soundtracks group by stk_alb_id, stk_album order by stk_album");
			}
			while(r.next()) {
				String gre = r.getString(5);
				if(gre.startsWith(", ")) {
					gre = gre.substring(2);
				}
				if(r.getString(2) != null) {
					if(r.getInt(3) == 1) {
						nodes.add(new DataNode(new Album(r.getInt(1), r.getString(2), r.getString(4), gre, r.getString(6), r.getInt(7), r.getString(8), r.getInt(9), r.getInt(10), r.getInt(11)),parent,null));
					} else {
						nodes.add(new DataNode(new Album(r.getInt(1), r.getString(2), r.getInt(3), gre, r.getString(6), r.getInt(7), r.getString(8), r.getInt(9), r.getInt(10), r.getInt(11)),parent,null));
					}
				} else {
					if(r.getInt(3) == 1) {
						nodes.add(new DataNode(new Album(0, "<no album>", r.getString(4), gre, r.getString(6), r.getInt(7), r.getString(8), r.getInt(9), r.getInt(10), r.getInt(11)),parent,null));
					} else {
						nodes.add(new DataNode(new Album(0, "<no album>", r.getInt(3), gre, r.getString(6), r.getInt(7), r.getString(8), r.getInt(9), r.getInt(10), r.getInt(11)),parent,null));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return nodes;
	}
}
