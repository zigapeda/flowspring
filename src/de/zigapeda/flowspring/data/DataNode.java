package de.zigapeda.flowspring.data;
 
import java.util.ArrayList;
import java.util.List;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.interfaces.TreeRow;

 
public class DataNode {
	
	private static DataNode library;
	private static DataNode search;
 
	private DataNode parent;
    private TreeRow data;
    private List<DataNode> children;
 
    public DataNode(List<DataNode> children) {
    	this.data = new Dummy();
    	this.parent = null;
        this.children = children;
        
        if (this.children == null) {
            this.children = new ArrayList<DataNode>();
        }
    }
    
    public DataNode(TreeRow data, DataNode parent, List<DataNode> children) {
        this.data = data;
        this.parent = parent;
        this.children = children;
 
        if (this.children == null) {
            this.children = new ArrayList<DataNode>();
        }
    }
 
    public TreeRow getData() {
    	return this.data;
    }
    
    public void setData(TreeRow data) {
    	this.data = data;
    }
    
    public DataNode getParent() {
    	return this.parent;
    }
    
    public boolean isRoot() {
    	if(this.parent == null) {
    		return true;
    	}
    	return false;
    }
 
    public List<DataNode> getChildren() {
    	if(this.children.size() == 0) {
//	    	if(this.data.getType() == TreeRow.Interpret) {
//	    		Connection c = Main.getDatabase();
//	            try {
//	    			Statement s = c.createStatement();
//	    			ResultSet r = s.executeQuery("select alb_name from albums, interprets where alb_int_id = int_id and int_name = '" + this.data.getArtist() + "';");
//	    	        while(r.next()) {
//	    				this.children.add(new DataNode(new Album(r.getString(1)),null));
//	    			}
//	    		} catch (SQLException e) {
//	    			e.printStackTrace();
//	    		}
//	    	} else if(this.data.getType() == TreeRow.Album) {
//	    		Connection c = Main.getDatabase();
//	            try {
//	    			Statement s = c.createStatement();
//	    			ResultSet r = s.executeQuery("select ttl_name from titles, albums where ttl_alb_id = alb_id and alb_name = '" + this.data.getAlbum() + "';");
//	    	        while(r.next()) {
//	    				this.children.add(new DataNode(new Title(r.getString(1)),null));
//	    			}
//	    		} catch (SQLException e) {
//	    			e.printStackTrace();
//	    		}
//	    	} else if(this.data.getType() == TreeRow.Title) {
//	    		return null;
//	    	}
    		if(this.data.getType() != TreeRow.Title) {
    			if(Main.getWindow() != null) {
    				Integer type = Main.getWindow().getControlllayout().getNextType(this.data.getType());
    				if(type != null) {
	    				switch(type) {
	    					case TreeRow.Title:
	    						this.children = Title.getTitles(this.getStatement(),this);
	    						break;
	    					case TreeRow.Interpret:
	    						this.children = Interpret.getInterprets(this.getStatement(),this);
	    						break;
	    					case TreeRow.Album:
	    						this.children = Album.getAlbums(this.getStatement(),this);
	    						break;
	    					case TreeRow.Genre:
	    						this.children = Genre.getGenres(this.getStatement(),this);
	    						break;
	    					case TreeRow.Track:
	    						this.children = Track.getTracks(this.getStatement(),this);
	    						break;
	    					case TreeRow.Year:
	    						this.children = Year.getYears(this.getStatement(),this);
	    						break;
	    					case TreeRow.Duration:
	    						this.children = Duration.getDurations(this.getStatement(),this);
	    						break;
	    					case TreeRow.Comment:
	    						this.children = Comment.getComments(this.getStatement(),this);
	    						break;
	    					case TreeRow.Rating:
	    						this.children = Rating.getRatings(this.getStatement(),this);
	    						break;
	    					case TreeRow.Playcount:
	    						this.children = Playcount.getPlaycounts(this.getStatement(),this);
	    						break;
	    				}
    				}
    			}
    		}
    	}
        return this.children;
    }
    
    public void setChildren(List<DataNode> children) {
    	this.children = children;
    }
    
    private String getStatement() {
    	String stmt = "";
		switch(this.data.getType()) {
			case TreeRow.Interpret:
				if(this.data.getId() != 0) {
					stmt = " stk_int_id = " + String.valueOf(this.data.getId()) + " ";
				} else {
					stmt = " stk_int_id is null ";
				}
				break;
			case TreeRow.Album:
				if(this.data.getId() != 0) {
					stmt = " stk_alb_id = " + String.valueOf(this.data.getId()) + " ";
				} else {
					stmt = " stk_alb_id is null ";
				}
				break;
			case TreeRow.Genre:
				if(this.data.getId() != 0) {
					stmt = " stk_gre_id = " + String.valueOf(this.data.getId()) + " ";
				} else {
					stmt = " stk_gre_id is null ";
				}
				break;
			case TreeRow.Track:
				stmt = " stk_track = " + String.valueOf(this.data.getTrack()) + " ";
				break;
			case TreeRow.Year:
				stmt = " stk_year = " + String.valueOf(this.data.getYear()) + " ";
				break;
			case TreeRow.Duration:
				stmt = " stk_duration = " + String.valueOf(this.data.getInt()) + " ";
				break;
			case TreeRow.Comment:
				if(this.data.getId() != 0) {
					stmt = " stk_com_id = " + String.valueOf(this.data.getId()) + " ";
				} else {
					stmt = " stk_com_id is null ";
				}
				break;
			case TreeRow.Rating:
				stmt = " stk_rating = " + String.valueOf(this.data.getInt()) + " ";
				break;
			case TreeRow.Playcount:
				stmt = " stk_playcount = " + String.valueOf(this.data.getPlaycount()) + " ";
				break;
		}
    	if(this.parent != null) {
    		stmt = this.parent.getStatement() + "and" + stmt;
    	}
    	return stmt;
    }

    public String toString() {
        return this.data.getName();
    }
    
    public static DataNode getLibrary() {
    	if(DataNode.library == null) {
    		DataNode.library = new DataNode(null);
    	}
    	return DataNode.library;
    }

	public static DataNode getSearch() {
    	if(DataNode.search == null) {
    		DataNode.search = new DataNode(null);
    	}
		return DataNode.search;
	}
    
    public static void startSearch(String searchstring) {
    	DataNode.search.setChildren(Title.getTitlesBySearchstring(searchstring));
    }
    
    public static void endSearch() {
    	
    }

	public static void refreshMedialib(Integer type) {
		if(type != null) {
			switch(type) {
				case TreeRow.Title:
					DataNode.getLibrary().setChildren(Title.getTitles(null,null));
					break;
				case TreeRow.Interpret:
					DataNode.getLibrary().setChildren(Interpret.getInterprets(null,null));
					break;
				case TreeRow.Album:
					DataNode.getLibrary().setChildren(Album.getAlbums(null,null));
					break;
				case TreeRow.Genre:
					DataNode.getLibrary().setChildren(Genre.getGenres(null,null));
					break;
				case TreeRow.Track:
					DataNode.getLibrary().setChildren(Track.getTracks(null,null));
					break;
				case TreeRow.Year:
					DataNode.getLibrary().setChildren(Year.getYears(null,null));
					break;
				case TreeRow.Duration:
					DataNode.getLibrary().setChildren(Duration.getDurations(null,null));
					break;
				case TreeRow.Comment:
					DataNode.getLibrary().setChildren(Comment.getComments(null,null));
					break;
				case TreeRow.Rating:
					DataNode.getLibrary().setChildren(Rating.getRatings(null,null));
					break;
				case TreeRow.Playcount:
					DataNode.getLibrary().setChildren(Playcount.getPlaycounts(null,null));
					break;
			}
		}
	}
}