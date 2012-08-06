package asakichy.architecture.distribution.data_transfer_object;

/**
 * Albumデータ変換クラス.
 */

public class AlbumDTO {
	private String title;
	private String artist;
	private TrackDTO[] tracks;

	public TrackDTO[] getTracks() {
		return tracks;
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

	public void setTracks(TrackDTO[] tracks) {
		this.tracks = tracks;
	}

}
