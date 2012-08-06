package asakichy.architecture.web_presentation.front_controller;

/**
 * Artistビューヘルパクラス.
 */

public class ArtistHelper {

	private Artist artist;

	public ArtistHelper(Artist artist) {
		this.artist = artist;
	}

	public String getName() {
		return artist.getName();
	}

	public String getLabel() {
		return artist.getLabel();
	}

}
