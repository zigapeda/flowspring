package de.zigapeda.flowspring.data;

public class PlaylistTrack {
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
}
