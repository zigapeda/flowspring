package de.zigapeda.flowspring.controller;

import java.io.File;

import de.zigapeda.flowspring.data.Title;

public class Rename {
	private static final String[] INVALID = new String[Character.MAX_VALUE];
	static {
	    for(int i=Character.MIN_VALUE;i<Character.MAX_VALUE;i++) {
	        INVALID[i] = Character.toString((char) i);
	    }
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			INVALID['\\'] = "_";
			INVALID['/'] = "_";
			INVALID[':'] = "_";
			INVALID['*'] = "_";
			INVALID['?'] = "_";
			INVALID['"'] = "_";
			INVALID['<'] = "_";
			INVALID['>'] = "_";
			INVALID['|'] = "_";
		} else if(System.getProperty("os.name").toLowerCase().contains("mac")) {
			INVALID['/'] = "_";
			INVALID[':'] = "_";
		} else {
			INVALID['/'] = "_";
		}
	}
	
	private static String musicdir;
	private static String dirstructure;
	private static String filestructure;
	
	public static boolean isFile(String path) {
		File f = new File(path);
		if(f.exists() == true) {
			if(f.isFile() == true) {
				return true;
			}
		}
		return false;
	}

	public static String createPath(Title t) {
		String path = getMusicDirectory() + getDirectoryStructure() + getFileStructure();
		int pos = t.getPath().lastIndexOf('.');
		if(pos != -1) {
			path = path + t.getPath().substring(pos).toLowerCase();
		}
		String artist = replaceInvalids(t.getArtist());
		String album = replaceInvalids(t.getAlbum());
		String title = replaceInvalids(t.getName());
		String track = replaceInvalids(t.getTrack());
		path = path.replaceAll("<interpret>", artist);
		path = path.replaceAll("<album>", album);
		path = path.replaceAll("<title>", title);
		path = path.replaceAll("<track>", track);
		return path;
	}
	
	public static String getMusicDirectory() {
		if(Rename.musicdir == null) {
			Rename.musicdir = Settings.loadSettings("defaultdir");
		}
		return Rename.musicdir;
	}
	
	public static String getDirectoryStructure() {
		if(Rename.dirstructure == null) {
			Rename.dirstructure = Settings.loadSettings("dirstructure");
		}
		return Rename.dirstructure;
	}
	
	public static String getFileStructure() {
		if(Rename.filestructure == null) {
			Rename.filestructure = Settings.loadSettings("filestructure");
		}
		return Rename.filestructure;
	}
	
	public static String replaceInvalids(String string) {
		if(string != null) {
		    StringBuilder sb = new StringBuilder(string.length());
		    for(int i=0;i<string.length();i++)
		        sb.append(INVALID[string.charAt(i)]);
		    return sb.toString();
		}
		return null;
	}
	
	public static boolean isRenameAvailable() {
		if(getMusicDirectory() != null && getDirectoryStructure() != null && getFileStructure() != null) {
			return true;
		}
		return false;
	}
	
}
