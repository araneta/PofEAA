package asakichy.object_relational.structural.dependent_mapping;

import java.util.List;

/**
 * Albumドメインオブジェクト.
 */

public class Album extends DomainObject {
	private String title;
	private List<Track> tracks;

	public Album(long id, String title, List<Track> tracks) {
		super(id);
		this.title = title;
		this.tracks = tracks;
	}

	public Album(long id) {
		this(id, "", null);
	}

	public String getTitle() {
		return title;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

	public void addTrack(Track track) {
		tracks.add(track);
	}

	public void clearTrack() {
		tracks.clear();
	}
}
