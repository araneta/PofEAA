package asakichy.object_relational.structural.foreign_key_mapping;

/**
 * Albumドメインオブジェクト.
 */

public class Album extends DomainObject {

	private String title;
	private Artist artist;

	public Album(long id, String title, Artist artist) {
		super(id);
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
