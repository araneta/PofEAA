package asakichy.architecture.distribution.data_transfer_object;

import java.util.ArrayList;
import java.util.List;

/**
 * Albumクラス.
 */

public class Album {
	private String title;
	private Artist artist;
	private List<Track> trackList;

	public Album(String title, Artist artist, List<Track> trackList) {
		this.title = title;
		this.artist = artist;
		this.trackList = trackList;
	}

	public Album(String title, Artist artist) {
		this(title, artist, new ArrayList<Track>());
	}

	public String getTitle() {
		return title;
	}

	public Artist getArtist() {
		return artist;
	}

	public List<Track> getTrackList() {
		return trackList;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public void setTrackList(List<Track> trackList) {
		this.trackList = trackList;
	}

	public void addTrack(Track track) {
		trackList.add(track);
	}

	public void clearTrack() {
		trackList.clear();
	}
}
