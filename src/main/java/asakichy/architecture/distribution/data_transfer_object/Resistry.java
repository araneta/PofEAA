package asakichy.architecture.distribution.data_transfer_object;

import java.util.HashMap;
import java.util.Map;

public class Resistry {

	private static Map<String, Artist> artists = new HashMap<String, Artist>();

	public static Artist findArtistByName(String name) {
		return artists.get(name);
	}

	public static void insertArtist(Artist artist) {
		artists.put(artist.getName(), artist);
	}

	public static void clearArtist() {
		artists.clear();
	}

	private static Map<String, Album> albums = new HashMap<String, Album>();

	public static Album findAlbum(String id) {
		return albums.get(id);
	}

	public static void insertAlbum(String id, Album album) {
		albums.put(id, album);
	}

	public static void clearAlbum() {
		albums.clear();
	}

}
