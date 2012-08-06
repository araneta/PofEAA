package asakichy.architecture.distribution.remote_facade;

/**
 * Albumデータ変換クラス.
 */

public class AlbumDTO {
	private String title;
	private String artist;

	public AlbumDTO(String title, String artist) {
		this.title = title;
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

}
