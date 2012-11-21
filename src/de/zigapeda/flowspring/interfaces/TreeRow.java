package de.zigapeda.flowspring.interfaces;

public interface TreeRow {
	public final static String[] RATINGS = {"☆☆☆☆☆","★☆☆☆☆","★★☆☆☆","★★★☆☆","★★★★☆","★★★★★"};
	
	public final static Type[] classes = {new Type("<Type>",1)
	                                     ,new Type("Interpret",2)
	                                     ,new Type("Album",3)
	                                     ,new Type("Genre",4)
	                                     ,new Type("Track",5)
	                                     ,new Type("Year",6)
	                                     ,new Type("Duration",7)
	                                     ,new Type("Comment",8)
	                                     ,new Type("Rating",9)
	                                     ,new Type("Playcount",10)};
	
	public final static int Dummy = 0;
	public final static int Title = 1;
	public final static int Interpret = 2;
	public final static int Album = 3;
	public final static int Genre = 4;
	public final static int Track = 5;
	public final static int Year = 6;
	public final static int Duration = 7;
	public final static int Comment = 8;
	public final static int Rating = 9;
	public final static int Playcount = 10;
	public final static int YoutubeSearch = 11;
	public final static int YoutubeVideo = 12;
	
	public int getId();
	public Integer getInt();
	public String getName();
	public String getArtist();
	public String getAlbum();
	public String getGenre();
	public String getTrack();
	public String getYear();
	public Integer getDuration();
	public String getComment();
	public String getRating();
	public String getPlaycount();
	public Integer getType();
	
	public class Type {
		private String name;
		private int type;
		
		public Type(String name, int type) {
			this.name = name;
			this.type = type;
		}
		
		public String toString() {
			return name;
		}
		
		public int getType() {
			return type;
		}
	}
}