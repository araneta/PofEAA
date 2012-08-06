package asakichy.architecture.distribution.remote_facade;

/**
 * Albumクラス.
 */

public class Album {
	private String title;
	private Artist artist;

	public Album(String title, Artist artist) {
		this.title = title;
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

}
