package de.zigapeda.flowspring.controller;

import java.nio.file.Path;

import de.zigapeda.flowspring.data.Title;
import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileFilter;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;

public class Tagreader {
	private Title title;
	
	public Tagreader(Path filepath) {
		AudioFile file;
		AudioFileFilter aff = new AudioFileFilter();
		this.title = null;
		if(filepath.toFile().canRead()) {
			try {
				if(aff.accept(filepath.toFile())) {
					file = AudioFileIO.read(filepath.toFile());
					Tag tag = null;
//					Ogg Vorbis Version 0
//					MPEG Version 1 (ISO/IEC 11172-3) || Layer III
					String encoding = file.getEncodingType();
					if(encoding.startsWith("MPEG")) {
						if(encoding.endsWith("Layer III")) {
							tag = file.getTag();
						}
					} else if(encoding.startsWith("Ogg")) {
						if(encoding.contains("Vorbis")) {
							tag = file.getTag();
						}
					}
					if(tag != null) {
						String name = null;
						String artist = null;
						String album = null;
						String genre = null;
						String comment = null;
						int track = 0;
						int year = 0;
						try {
							track = Integer.valueOf(tag.getFirstTrack());
						} catch(NumberFormatException e) {
						}
						try {
							year = Integer.valueOf(tag.getFirstYear());
						} catch(NumberFormatException e) {
						}
						if(!(genre = tag.getFirstGenre()).equals("")) {
							if(genre.startsWith("(")) {
								if(genre.contains(")")) {
									genre = Genrelist.getGenre(genre);
								}
							}
						}
						if((album = tag.getFirstAlbum()).equals("")) {
							album = null;
						}
						if((artist = tag.getFirstArtist()).equals("")) {
							artist = null;
						}
						if((comment = tag.getFirstComment()).equals("")) {
							comment = null;
						}
						if((name = tag.getFirstTitle()).equals("")) {
							name = filepath.getFileName().toString();
						}
						this.title = new Title(0, name, artist, album, genre, comment, track, year, file.getLength(), 0, 0, filepath.toString());
					}
				}
			} catch (Exception e) {
			}
		}
	}
	
	public Title getTitle() {
		return this.title;
	}
}
